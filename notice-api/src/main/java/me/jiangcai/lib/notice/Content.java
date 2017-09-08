package me.jiangcai.lib.notice;

import java.util.Map;

/**
 * 通知内容
 *
 * @author CJ
 */
public interface Content {
    /**
     * @return 文本的描述方式
     */
    String asText();

    /**
     * @return 签名名称
     * @since 3.0
     */
    String signName();

    /**
     * @return 模板名称
     * @since 3.0
     */
    String templateName();

    /**
     * @return 模板参数值
     * @since 3.0
     */
    Map<String, ?> templateParameters();
}
