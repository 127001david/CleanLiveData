package com.rightpoint.oknet.vm;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.rightpoint.oknet.ok.RetrofitService;
import com.rightpoint.oknet.ok.UnPeekLiveData;
import com.rightpoint.oknet.okservice.IEpidemicService;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Description：
 * @author Wonder Wei
 * Create date：2020/11/6 2:36 PM 
 */
public class EpidemicViewModel extends ViewModel {
    public static final String TAG = "EpidemicViewModel";

    public UnPeekLiveData<String> epidemicLiveData;
    private final IEpidemicService mEpidemicService;

    public EpidemicViewModel() {
        epidemicLiveData = new UnPeekLiveData<>();
        mEpidemicService = RetrofitService.createService(IEpidemicService.class);
    }

    public void loadEpidemic() {
        mEpidemicService.requestHomeExamList().enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NotNull Call<String> call
                    , @NotNull Response<String> response) {
                if (response.isSuccessful()) {
                    epidemicLiveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NotNull Call<String> call
                    , @NotNull Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });

    }
}
