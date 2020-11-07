package com.rightpoint.oknet.okservice;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Description：
 * @author Wonder Wei
 * Create date：2020/11/6 2:23 PM 
 */
public interface IEpidemicService {
    @GET("127001david/CleanLiveData")
    Call<String> requestHomeExamList();
}
