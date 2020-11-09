package com.rightpoint.oknet.ok;

/**
 * Description：
 * @author Wonder Wei
 * Create date：2020/11/9 2:00 PM
 */
public class HttpResult<T> {

    public String code;

    public T result;

    public String errorMsg;

    public boolean success;
}
