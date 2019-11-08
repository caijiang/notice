package me.jiangcai.lib.notice.supplier;

import me.jiangcai.lib.notice.Content;
import me.jiangcai.lib.notice.EmailNoticeSupplier;
import me.jiangcai.lib.notice.To;
import me.jiangcai.lib.notice.email.EmailAddress;
import me.jiangcai.lib.notice.exception.NoticeException;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.NumberUtils;
import org.springframework.util.StringUtils;

import javax.activation.DataSource;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * @author CJ
 */
@SuppressWarnings("unused")
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class EmailNoticeSupplierImpl implements EmailNoticeSupplier {

    private final String host;
    private final String sslPort;
    private final String port;
    private final String username;
    private final String password;
    private final EmailAddress fromEmail;

    public EmailNoticeSupplierImpl(Properties environment) {
//        this.environment = environment;
        host = environment.getProperty("smtp.host");
        sslPort = environment.getProperty("smtp.sslPort");
        String port;
        if (StringUtils.isEmpty(sslPort)) {
            port = environment.getProperty("smtp.port");
        } else {
            port = environment.getProperty("smtp.port");
        }
        this.port = port;
        this.username = environment.getProperty("smtp.username");
        this.password = environment.getProperty("smtp.password");
        this.fromEmail = new EmailAddress(
                environment.getProperty("from.name")
                , environment.getProperty("from.email")
        );
    }

    @Override
    public void send(To to, Content content) throws NoticeException {
        final Set<EmailAddress> emailTo = to.emailTo();
        if (CollectionUtils.isEmpty(emailTo)) {
            throw new NoticeException("发送地址是空");
        }
        HtmlEmail email = new HtmlEmail();
        email.setCharset("UTF-8");


        try {
            // 设置目标地址
            for (EmailAddress address : emailTo) {
                if (StringUtils.isEmpty(address.getName())) {
                    email.addTo(address.getEmail());
                } else {
                    email.addTo(address.getEmail(), address.getName(), "UTF-8");
                }
            }


            // 设置邮件内容
            String html;
            final List<DataSource> attachments = content.embedAttachments();
            if (!CollectionUtils.isEmpty(attachments)) {
                HashMap<String, String> refs = new HashMap<>();
                for (DataSource dataSource : attachments) {
                    String contentId = email.embed(dataSource, dataSource.getName());
                    refs.put(dataSource.getName(), "cid:" + contentId);
                }
                html = content.asHtml(refs);
            } else {
                html = content.asHtml(null);
            }

            // 主要内容
            email.setHtmlMsg(html);
            email.setSubject(content.asTitle());

            // 附件内容
            final List<DataSource> otherAttachments = content.otherAttachments();
            if (!CollectionUtils.isEmpty(otherAttachments)) {
                for (DataSource dataSource : otherAttachments) {
//                    email.addPart(new MimeMultipart(dataSource));
                    email.attach(dataSource, dataSource.getName(), "");
                }
            }

            // 系统信息
            email.setHostName(host);
            if (!StringUtils.isEmpty(sslPort)) {
                email.setSslSmtpPort(sslPort);
                email.setSSLOnConnect(true);
            } else {
                email.setSmtpPort(NumberUtils.parseNumber(port, Integer.class));
                email.setSSLOnConnect(false);
            }
            if (!StringUtils.isEmpty(username))
                email.setAuthentication(username, password);
            email.setFrom(fromEmail.getEmail(), fromEmail.getName(), "UTF-8");

            email.send();
        } catch (EmailException ex) {
            throw new NoticeException(ex);
        }


    }

    @Override
    public void statusReport() throws IOException {

    }
}
