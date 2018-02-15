package me.jiangcai.lib.notice;

import me.jiangcai.lib.notice.email.EmailAddress;

import java.util.Set;

/**
 * 收件人
 *
 * @author CJ
 */
public interface To {

    /**
     * @return 移动号码
     */
    String mobilePhone();

    Set<EmailAddress> emailTo();
}
