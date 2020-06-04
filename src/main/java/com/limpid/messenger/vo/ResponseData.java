package com.limpid.messenger.vo;

import com.limpid.messenger.enumeration.GlobalConstant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 返回对象定义
 *
 * @auther cuiqiongyu
 * @create 2020/5/20 14:45
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseData<T> implements Serializable {

    private static final long serialVersionUID = -295323839556640626L;

    private String code;
    private String message = "";
    private T data;

    public ResponseData(GlobalConstant.ResponseState responseState, T data) {
        this.data = data;
        this.code = responseState.getCode();
        this.message = responseState.getMessage();
    }

}
