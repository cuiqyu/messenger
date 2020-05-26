package com.limpid.messenger.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Map;

/**
 * 短信模板配置
 *
 * @auther cuiqiongyu
 * @create 2020/5/26 15:04
 */
@Configuration
@ConfigurationProperties(prefix = "sms.ali")
@PropertySource(value = "classpath:/config/sms_config.yml", encoding = "utf-8", factory = CustomerPropertySourceFactory.class)
@Data
public class AliSmsConfig {

    private String signName;
    private Map<Integer, String> templateCodeMap;
    private String accessKeyId;
    private String accessKeySecret;
    private String action;

}
