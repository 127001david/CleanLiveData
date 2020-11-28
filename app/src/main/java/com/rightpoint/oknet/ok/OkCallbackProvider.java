package com.rightpoint.oknet.ok;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Description：提供一个 OkCallback，对 retrofit2.Callback 进行了封装
 * ，对接口返回值做统一的预处理，适合在项目中使用，当然也可以选择直接使用 retrofit2.Callback。
 * @author Wonder Wei
 * Create date：2020/11/9 1:58 PM 
 */
public class OkCallbackProvider {

    public static <T> Callback<HttpResult<T>> getCallback(@NonNull OkCallback<T> callback) {
        return new Callback<HttpResult<T>>() {
            @Override
            public void onResponse(@NotNull Call<HttpResult<T>> call,
                                   @NotNull Response<HttpResult<T>> response) {
                if (response.isSuccessful() && null != response.body()) {
                    if (response.body().code.equals("0")) {
                        callback.onResponse(response.body().result);
                    } else {
                        callback.onFailure(new OkError("-1", response.body().errorMsg));
                    }
                } else {
                    callback.onFailure(new OkError("-1", "请求失败"));
                }
            }

            @Override
            public void onFailure(@NotNull Call<HttpResult<T>> call, @NotNull Throwable t) {
                callback.onFailure(new OkError("-1", t.toString()));
            }
        };
    }
}
