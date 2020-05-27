package com.limpid.messenger.util;

import com.google.common.collect.Lists;
import com.limpid.messenger.enumeration.GlobalConstant;
import com.limpid.messenger.exception.CustomException;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.List;

/**
 * 断言自定义异常
 *
 * @auther cuiqiongyu
 * @create 2019/12/10 16:39
 */
@NoArgsConstructor
public abstract class CustomExceptionAssert {

    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new CustomException(message);
        }
    }

    public static void isTrue(boolean expression) {
        isTrue(expression, "this expression must be true");
    }

    public static void isNull(Object object, String message) {
        if (object != null) {
            throw new CustomException(message);
        }
    }

    public static void isNull(Object object) {
        isNull(object, "the object argument must be null");
    }

    public static void notNull(Object object, GlobalConstant.ResponseState state) {
        if (object == null) {
            throw new CustomException(state);
        }
    }

    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new CustomException(message);
        }
    }

    public static void notNull(Object object) {
        notNull(object, "this argument is required; it must not be null");
    }

    public static void notEmpty(String str, GlobalConstant.ResponseState state) {
        if (StringUtils.isEmpty(str)) {
            throw new CustomException(state);
        }
    }

    public static void notEmpty(String str, String message) {
        if (StringUtils.isEmpty(str)) {
            throw new CustomException(message);
        }
    }

    public static void notEmpty(Collection<?> collection, String message) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new CustomException(message);
        }
    }

    public static void notEmpty(Collection<?> collection) {
        notEmpty(collection, "this collection must not be empty: it must contain at least 1 element");
    }

    public static void allNotNullEmpty(String message, Object obj, Object... objects) {
        List<Object> objectList = Lists.newArrayList(objects);
        objectList.add(obj);
        for (Object object : objectList) {
            if (object instanceof Collection) {
                notEmpty((Collection<?>) object, message);
            } else {
                notNull(object, message);
            }
        }
    }

    public static void hasLength(String text, String message) {
        if (!StringUtils.hasLength(text)) {
            throw new CustomException(message);
        }
    }

    public static void hasLength(String text) {
        hasLength(text, "this String argument must have length; it must not be null or empty");
    }

    public static void hasText(String text, String message) {
        if (!StringUtils.hasText(text)) {
            throw new CustomException(message);
        }
    }

    public static void hasText(String text) {
        hasText(text, "this String argument must have text; it must not be null, empty, or blank");
    }

}
