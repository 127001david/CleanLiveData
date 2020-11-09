package com.rightpoint.oknet.ok;

/**
 * Description：
 * @author Wonder Wei
 * Create date：2020/11/9 2:23 PM 
 */
public interface OkCallback<T> {
    void onResponse(T response);

    void onFailure(String error);
}
