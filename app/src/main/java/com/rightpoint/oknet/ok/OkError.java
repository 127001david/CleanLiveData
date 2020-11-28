package com.rightpoint.oknet.ok;

/**
 * Description：
 * @author Wonder Wei
 * Create date：2020/11/10 2:27 PM 
 */
public class OkError {
    public final String code;

    public final String errorMsg;

    public OkError(String code, String errorMsg) {
        this.code = code;
        this.errorMsg = errorMsg;
    }
}
