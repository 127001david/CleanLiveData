package com.rightpoint.oknet.ok;

import retrofit2.Retrofit;

/**
 * Description：
 * @author Wonder Wei
 * Create date：2020/11/6 2:24 PM 
 */
public class RetrofitService {
    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://github.com/")
            .addConverterFactory(SimpleGsonConverterFactory.create())
            .build();

    public static <T> T createService(Class<T> serviceClass) {
        return retrofit.create(serviceClass);
    }
}
