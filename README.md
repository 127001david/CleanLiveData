# CleanLiveData
相同 LifecycleOwner、Observer 多次注册到 LiveData（比如在 onResume里注册，在 onPause 里反注册）时会重复收到粘滞数据，这种情况有博客描述为“数据倒灌”，我觉得叫“数据回流”也可以，不管叫什么，这种收到重复数据的现象相信在项目中应用到 Lifecycle 组件的同学应该都遇见过，而且针对这个问题应该也造好自己的轮子了，我这里记录的是我结合 LiveData 数据版本控制以及 SingleLiveEvent 数据过滤而实现的一种基本保持 LiveData 原本使用习惯的解决思路。

## 使用方法
防止数据回流的实现只有 CleanLiveData 这一个类，使用者把类直接 copy 到项目中的合适位置，然后用 CleanLiveData 替换掉原本的 MutableLiveData（如果你用的是 LiveData 的其他派生类，那么只要把 CleanLiveData 粘到项目里然后将超类  MutableLiveData  替换为你需要的类） 就好。

**注：以下是类 CleanLiveData 的实现解析**

## 数据的版本控制
在思考如何解决数据回流的时候我的第一反应就是能不能给每条数据和数据接收者 observer 都加上版本，然后在接收数据时判断当前数据的版本是不是新于也就是大于 observer 已经接收到的数据的版本，如果是新数据就处理，老数据就不处理。
不过在想要着手实施的时候就发现了这种实现的问题，就是代码的侵入性会很强，需要替换原有 observer 和数据类，这样的修改面积太大，相当于一次重构了。还是得想想有没有侵入性更低的办法。
其实答案就在 LiveData 的源码中，在 LiveData 分发数据时是有对数据和 observer 版本进行判断的操作的：
```
private void considerNotify(ObserverWrapper observer) {
        if (!observer.mActive) {
            return;
        }
        // Check latest state b4 dispatch. Maybe it changed state but we didn't get the event yet.
        //
        // we still first check observer.active to keep it as the entrance for events. So even if
        // the observer moved to an active state, if we've not received that event, we better not
        // notify for a more predictable notification order.
        if (!observer.shouldBeActive()) {
            observer.activeStateChanged(false);
            return;
        }
		// 版本判断就在这里
        if (observer.mLastVersion >= mVersion) {
            return;
        }
        observer.mLastVersion = mVersion;
        //noinspection unchecked
        observer.mObserver.onChanged((T) mData);
    }
```
就是此方法将数据分发到各个 observer 的 onChanged 回调中的，可以看到这个方法就是通过对 observer.mLastVersion >= mVersion 的比较来判断是否向 observer 发送数据的，那么这里就有个问题了，既然数据和 observer 都是有版本的怎么还会发生数据回流的现象呢？看方法的参数列表就可以发现这里的 observer 并不是我们注册时传入的 Observer 类型，而是一种新的 LiveData 私有的内部类型 ObserverWrapper，这个 ObserverWrapper 是个包装类，里面包装了 observer 和一个版本号，而这个包装类的实例是在每次注册调用 
`void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer)`
方法时实例化的，这也就解释了为什么数据和 observer 都有版本号为什么还是会收到重复消息了，因为这里的 observer 并不是我们传入的那个 observer 而是一个经过包装的 ObserverWrapper，每次注册都会有一个新的 ObserverWrapper 新的 ObserverWrapper 的 mLastVersion 是默认值 -1，那么每次注册时就会收到一份粘滞数据也就是必然的了，这就是所谓的“设计如此”。
看到这里解题思路也就出来了，对数据添加版本的思路是没错的，我们只需要继承 LiveData（具体实现时 CleanLiveData 派生自 MutableLiveData 类，因为这个是 LiveData 比较常用的派生类，如果使用者有其他需求，依样画葫芦，选择合适的类继承就可以了） 类并重写 observer 方法，对传入的 observer 也进行一次包装，包装类干脆还叫 ObserverWrapper 好了，里面先不干别的，就做版本控制，不过这次的包装类对象并不是每次都实例化，而是存在一个 HashMap 里，如果 map 里不存在这个 observer 就新建一个并塞进去，如果存在就拿出来并调用 super.observer 方法注册。这样我们就有了自己的数据版本控制，而且不会每次新建，只要在数据分发的时候判断这个版本号就可以对 observer 进行过滤，收到过数据就不需要在分发了。
至于数据分发和版本判断部分就是借鉴的 SingleLiveEvent 里数据分发的部分了：
```

@MainThread
public void observe(LifecycleOwner owner, final Observer<? super T> observer) {
        if (hasActiveObservers()) {
                Log.w(TAG, "Multiple observers registered but only one will be notified of changes.");
        }

        // Observe the internal MutableLiveData
        super.observe(owner, new Observer<T>() {
                @Override
                public void onChanged(@Nullable T t) {
                        if (mPending.compareAndSet(true, false)) {
                             observer.onChanged(t);
                         }
                }
        });
}
```
对 observer 进行包装分发数据的时候添加判断，我们已经有了包装类 ObserverWrapper 了，只要在类的 onChange 方法中添加版本控制就可以了：
```
private boolean received(Observer<? super T> observer) {
        if (null != observerWrappers.get(observer.hashCode())) {
            ObserverWrapper observerWrapper = observerWrappers.get(observer.hashCode());
            // observer 上次接收到的消息版本大于等于当前消息的版本时跳过这个 observer
            if (observerWrapper.mLastVersion >= mVersion) {
                return true;
            } else {
                observerWrapper.mLastVersion = mVersion;
                return false;
            }
        }
        return false;
    }

    private class ObserverWrapper implements Observer<T> {
        final Observer<? super T> mObserver;
        /**
         * 记录 Observer 上次接收到的消息版本
         */
        int mLastVersion = -1;

        ObserverWrapper(Observer<? super T> observer) {
            mObserver = observer;
        }

        @Override
        public void onChanged(T t) {
            if (!received(mObserver)) {
                mObserver.onChanged(t);
            }
        }
    }
```

以上完毕
