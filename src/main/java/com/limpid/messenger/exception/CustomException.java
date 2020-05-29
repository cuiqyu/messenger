package com.limpid.messenger.exception;

import com.limpid.messenger.enumeration.GlobalConstant;
import lombok.Data;
import lombok.Getter;

/**
 * 自定义业务异常
 *
 * @auther cuiqiongyu
 * @create 2020/5/20 15:41
 */
@Getter
public class CustomException extends RuntimeException {

    private static final long serialVersionUID = 8316873551194247424L;

    private String code;

    private Object data;

    public CustomException(String message) {
        super(message);
        this.code = GlobalConstant.ResponseState.BUSINESS_ERROR.getCode();
    }

    public CustomException(GlobalConstant.ResponseState state) {
        super(state.getMessage());
        this.code = state.getCode();
    }

    public CustomException(String code, String message) {
        super(message);
        this.code = code;
    }

    public CustomException(String code, String message, Object data) {
        super(message);
        this.code = code;
        this.data = data;
    }

    public CustomException(String code, Throwable throwable) {
        super(throwable);
        this.code = code;
    }


    public CustomException(String message, Throwable throwable, String code) {
        super(message, throwable);
        this.code = code;
    }

    public CustomException(String message, Throwable throwable, boolean enableSuppression, boolean writableStackTrace, String code) {
        super(message, throwable, enableSuppression, writableStackTrace);
        this.code = code;
    }

}
