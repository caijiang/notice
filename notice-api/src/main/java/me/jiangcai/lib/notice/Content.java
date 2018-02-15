package me.jiangcai.lib.notice;

import javax.activation.DataSource;
import java.util.List;
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
     * @return 嵌入到正文中的附件; null 表示不需要
     * @since 4.0
     */
    List<DataSource> embedAttachments();

    /**
     * @return 其他附件;null 表示不需要
     * @since 4.0
     */
    List<DataSource> otherAttachments();

    /**
     * @param attachmentRefs 嵌入到附件的地址；有可能是null
     * @return html的描述方式
     * @since 4.0
     */
    String asHtml(Map<String, String> attachmentRefs);

    /**
     * @return 标题
     * @since 4.0
     */
    String asTitle();

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
