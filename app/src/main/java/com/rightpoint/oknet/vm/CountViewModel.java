package com.rightpoint.oknet.vm;

import androidx.lifecycle.ViewModel;

import com.rightpoint.oknet.ok.UnPeekLiveData;

/**
 * Description：
 * @author Wonder Wei
 * Create date：2020/11/5 6:59 AM 
 */
public class CountViewModel extends ViewModel {
    private int mCount;
    public UnPeekLiveData<Integer> countLiveData;

    public CountViewModel() {
        countLiveData = new UnPeekLiveData<>();
    }

    public void increase() {
        countLiveData.postValue(++mCount);
    }
}
