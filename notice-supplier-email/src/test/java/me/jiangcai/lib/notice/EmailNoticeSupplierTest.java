package me.jiangcai.lib.notice;

import me.jiangcai.lib.notice.email.EmailAddress;
import me.jiangcai.lib.test.SpringWebTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;

import javax.activation.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author CJ
 */
@ContextConfiguration(classes = {EmailNoticeSupplierTest.Config.class, NoticeSpringConfig.class})
public class EmailNoticeSupplierTest extends SpringWebTest {

    @Autowired
    private EmailNoticeSupplier emailNoticeSupplier;

    @Test
    public void go() {
        System.out.println(emailNoticeSupplier);
        emailNoticeSupplier.send(new To() {
            @Override
            public String mobilePhone() {
                return null;
            }

            @Override
            public Set<EmailAddress> emailTo() {
                return Collections.singleton(new EmailAddress("蒋才", "caijiang@mingshz.com"));
            }
        }, new Content() {
            @Override
            public String asText() {
                return null;
            }

            @Override
            public List<DataSource> embedAttachments() {
                return Collections.singletonList(new DataSource() {
                    @Override
                    public InputStream getInputStream() throws IOException {
                        return new ClassPathResource("/image_test.png").getInputStream();
                    }

                    @Override
                    public OutputStream getOutputStream() throws IOException {
                        throw new IllegalStateException("getOutputStream not supported");
                    }

                    @Override
                    public String getContentType() {
                        return "image/png";
                    }

                    @Override
                    public String getName() {
                        return "pic";
                    }
                });
            }

            @Override
            public List<DataSource> otherAttachments() {
                return Collections.singletonList(new DataSource() {
                    @Override
                    public InputStream getInputStream() throws IOException {
                        return new ClassPathResource("/image_test.png").getInputStream();
                    }

                    @Override
                    public OutputStream getOutputStream() throws IOException {
                        throw new IllegalStateException("getOutputStream not supported");
                    }

                    @Override
                    public String getContentType() {
                        return "image/png";
                    }

                    @Override
                    public String getName() {
                        return "图片";
                    }
                });
            }

            @Override
            public String asHtml(Map<String, String> attachmentRefs) {
                return "<p><img src=\"" + attachmentRefs.get("pic") + "\"/>中文？</p>";
            }

            @Override
            public String asTitle() {
                return "测试邮件么？";
            }

            @Override
            public String signName() {
                return null;
            }

            @Override
            public String templateName() {
                return null;
            }

            @Override
            public Map<String, ?> templateParameters() {
                return null;
            }
        });
    }

    @Configuration
    @PropertySource("classpath:/email_test.properties")
    public static class Config {

    }

}