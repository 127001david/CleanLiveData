package com.rightpoint.oknet.vm;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.rightpoint.oknet.ok.NoBackflowLiveData;

/**
 * Description：
 * @author Wonder Wei
 * Create date：2020/11/5 6:59 AM 
 */
public class CountViewModel extends ViewModel {
    private int mCount;
    /**
     * 直接继承自 MutableLiveData 会发生重复接收
     */
    public MutableLiveData<Integer> countLiveData;

    public CountViewModel() {
        countLiveData = new NoBackflowLiveData<>();
    }

    public void increase() {
        countLiveData.postValue(++mCount);
    }
}
