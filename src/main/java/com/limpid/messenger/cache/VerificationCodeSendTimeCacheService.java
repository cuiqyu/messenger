package com.limpid.messenger.cache;

import com.limpid.messenger.config.AliSmsConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 验证码发送时间缓存
 *
 * @auther cuiqiongyu
 * @create 2020/5/27 16:17
 */
@Service
public class VerificationCodeSendTimeCacheService {

    @Autowired
    private GuavaCache<Long> verificationCodeSendTimeCache;

    @Autowired
    private AliSmsConfig aliSmsConfig;

    /**
     * 存入手机号本次发送验证码的时间戳
     *
     * @param cellphone
     * @param reqTime
     */
    public void putCache(String cellphone, Long reqTime) {
        verificationCodeSendTimeCache.put(cellphone, reqTime);
    }

    /**
     * 请求手机号上一次发送验证码的时间戳
     *
     * @param cellphone
     * @return
     */
    public Long getCache(String cellphone) {
        return verificationCodeSendTimeCache.getCache(cellphone);
    }

    /**
     * 校验手机是否可以发送验证码
     *
     * @param cellphone
     * @return
     */
    public boolean checkSendVerificationCode(String cellphone, Long currentTimeMillis) {
        if (StringUtils.isEmpty(cellphone)) {
            return false;
        }

        // 查询该手机号上一次发送验证码的时间戳
        Long cache = getCache(cellphone);
        // 获取当前时间戳
        if (null == cache || null == aliSmsConfig.getVerificationCodeInterval()
                || aliSmsConfig.getVerificationCodeInterval().compareTo((int) (currentTimeMillis - cache) / 1000) < 1) {
            // 未要求满足时间间隔或已经满足时间间隔，允许手机号发送验证码
            return true;
        }

        return false;
    }

}
