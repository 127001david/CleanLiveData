package com.rightpoint.oknet.ok;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.HashMap;

/**
 * Description：
 * @author Wonder Wei
 * Create date：2020/11/6 4:23 PM 
 */
public class UnPeekLiveData<T> extends MutableLiveData<T> {
    private int mVersion = -1;

    private final HashMap<Integer, ObserverWrapper> observerWrappers = new HashMap<>();

    public int getVersion() {
        return mVersion;
    }

    @Override
    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
        ObserverWrapper observerWrapper = observerWrappers.get(owner.hashCode());
        if (null == observerWrapper) {
            observerWrapper = new ObserverWrapper(owner, observer);
            observerWrappers.put(owner.hashCode(), observerWrapper);
        }
        super.observe(owner, observerWrapper);
    }

    public void removeObserver(@NonNull LifecycleOwner owner) {
        super.removeObserver(observerWrappers.get(owner.hashCode()));
    }

    @Override
    public void setValue(T value) {
        mVersion++;
        super.setValue(value);
    }

    private boolean received(LifecycleOwner owner) {
        if (null != observerWrappers.get(owner.hashCode())) {
            ObserverWrapper observerWrapper = observerWrappers.get(owner.hashCode());
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
        final LifecycleOwner mOwner;
        int mLastVersion = -1;

        ObserverWrapper(@NonNull LifecycleOwner owner, Observer<? super T> observer) {
            mOwner = owner;
            mObserver = observer;
        }

        @Override
        public void onChanged(T t) {
            if (!received(mOwner)) {
                mObserver.onChanged(t);
            }
        }
    }
}
