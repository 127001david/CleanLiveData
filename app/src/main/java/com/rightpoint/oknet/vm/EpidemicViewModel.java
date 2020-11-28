package com.rightpoint.oknet.vm;

import androidx.lifecycle.ViewModel;

import com.rightpoint.oknet.ok.CleanLiveData;
import com.rightpoint.oknet.ok.OkCallback;
import com.rightpoint.oknet.ok.OkCallbackProvider;
import com.rightpoint.oknet.ok.OkError;
import com.rightpoint.oknet.ok.RetrofitService;
import com.rightpoint.oknet.okservice.IEpidemicService;

/**
 * Description：
 * @author Wonder Wei
 * Create date：2020/11/6 2:36 PM 
 */
public class EpidemicViewModel extends ViewModel {
    public static final String TAG = "EpidemicViewModel";

    /**
     * 不会重复接收消息
     */
    public CleanLiveData<String> epidemicLiveData;
    private final IEpidemicService mEpidemicService;

    public EpidemicViewModel() {
        epidemicLiveData = new CleanLiveData<>();
        mEpidemicService = RetrofitService.createService(IEpidemicService.class);
    }

    public void loadEpidemic() {
        mEpidemicService.requestHomeExamList()
                .enqueue(OkCallbackProvider.getCallback(new OkCallback<String>() {
                    @Override
                    public void onResponse(String response) {
                        epidemicLiveData.postValue(response);
                    }

                    @Override
                    public void onFailure(OkError error) {
                        epidemicLiveData.postError(error);
                    }
                }));
    }
}
