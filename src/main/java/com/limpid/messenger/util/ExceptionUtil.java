package com.limpid.messenger.util;

/**
 * 异常工具类
 *
 * @auther cuiqiongyu
 * @create 2020/5/27 9:44
 */
public class ExceptionUtil {

    public static String getDesc(Throwable throwable) {
        String message = throwable.getMessage();
        if (message != null && message.contains("Exception:")) {
            message = message.substring(message.lastIndexOf("Exception:") + 10);
        }
        return message;
    }

}
