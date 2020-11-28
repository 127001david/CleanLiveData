package com.rightpoint.oknet.okservice;

import com.rightpoint.oknet.ok.HttpResult;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Description：
 * @author Wonder Wei
 * Create date：2020/11/6 2:23 PM 
 */
public interface IEpidemicService {
    // FIXME: 2020/11/28 这就是个 mock 接口，请求可以返回但是 Gson 解析会出错，仅做演示用
    @GET("127001david/CleanLiveData")
    Call<HttpResult<String>> requestHomeExamList();
}
