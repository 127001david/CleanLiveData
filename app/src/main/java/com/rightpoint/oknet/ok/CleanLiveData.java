package com.rightpoint.oknet.ok;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.HashMap;

/**
 * Description：同一 Observer 多次注册，仅第一次会收到粘滞消息
 * @author Wonder Wei
 * Create date：2020/11/6 4:23 PM 
 */
public class CleanLiveData<T> extends MutableLiveData<T> {
    /**
     * 记录当前消息版本
     */
    private int mVersion = -1;

    private final HashMap<Integer, ObserverWrapper> observerWrappers = new HashMap<>();

    public int getVersion() {
        return mVersion;
    }

    @Override
    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
        ObserverWrapper observerWrapper = observerWrappers.get(observer.hashCode());
        if (null == observerWrapper) {
            observerWrapper = new ObserverWrapper(observer);
            observerWrappers.put(observer.hashCode(), observerWrapper);
        }
        super.observe(owner, observerWrapper);
    }

    public void removeObserver(@NonNull Observer<? super T> observer) {
        ObserverWrapper observerWrapper = observerWrappers.get(observer.hashCode());
        if (null != observerWrapper) {
            super.removeObserver(observerWrapper);
        }
    }

    @Override
    public void setValue(T value) {
        mVersion++;
        super.setValue(value);
    }

    private boolean received(Observer<? super T> observer) {
        ObserverWrapper observerWrapper = observerWrappers.get(observer.hashCode());
        if (null != observerWrapper) {
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
}
