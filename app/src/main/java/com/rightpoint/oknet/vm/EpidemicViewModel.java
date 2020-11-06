package com.rightpoint.oknet.vm;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.rightpoint.oknet.module.Epidemic;
import com.rightpoint.oknet.ok.HttpResult;
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

    public UnPeekLiveData<Epidemic> epidemicLiveData;
    private final IEpidemicService mEpidemicService;

    public EpidemicViewModel() {
        epidemicLiveData = new UnPeekLiveData<>();
        mEpidemicService = RetrofitService.createService(IEpidemicService.class);
    }

    public void loadEpidemic() {
        mEpidemicService.requestHomeExamList(1).enqueue(new Callback<HttpResult<Epidemic>>() {
            @Override
            public void onResponse(@NotNull Call<HttpResult<Epidemic>> call
                    , @NotNull Response<HttpResult<Epidemic>> response) {
                if (response.isSuccessful()) {
                    epidemicLiveData.postValue(response.body().getResult());
                }
            }

            @Override
            public void onFailure(@NotNull Call<HttpResult<Epidemic>> call
                    , @NotNull Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });

    }
}
