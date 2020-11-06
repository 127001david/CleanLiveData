package com.rightpoint.oknet.okservice;

import com.rightpoint.oknet.module.Epidemic;
import com.rightpoint.oknet.ok.HttpResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Description：
 * @author Wonder Wei
 * Create date：2020/11/6 2:23 PM 
 */
public interface IEpidemicService {
    @GET("wx/epidemic/info/list?columnId=46,47,48,49&limit=10")
    Call<HttpResult<Epidemic>> requestHomeExamList(@Query("page") int page);
}
