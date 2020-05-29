package com.limpid.messenger.vo.client.ali;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 阿里云发送短信返回结果
 *
 * @auther cuiqiongyu
 * @create 2020/5/28 15:04
 */
@Data
public class AliSengSmsResponse implements Serializable {

    private static final long serialVersionUID = -1365698449012588234L;

    /**
     * 签名名称
     */
    @JSONField(name = "SignName")
    private String signName;
    /**
     * 状态码的描述
     */
    @JSONField(name = "Message")
    private String message;
    /**
     * 请求ID
     */
    @JSONField(name = "RequestId")
    private String requestId;
    /**
     * 状态码：
     * OK：接口调用成功
     * <p>
     * 其他枚举详见：https://help.aliyun.com/document_detail/101346.html?spm=a2c1g.8271268.10000.143.69dbdf25cuYGzo
     */
    @JSONField(name = "Code")
    private String code;

}
