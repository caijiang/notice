package me.jiangcai.lib.notice;

import me.jiangcai.lib.notice.email.EmailAddress;
import me.jiangcai.lib.test.SpringWebTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;

import javax.activation.DataSource;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * @author CJ
 */
@ContextConfiguration(classes = {NoticeSpringConfig.class})
public class AliyunNoticeSupplierTest extends SpringWebTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void go() throws IOException {
        Properties ps = new Properties();
        ps.load(new ClassPathResource("local_aliyun.properties").getInputStream());

        AliyunNoticeSupplier supplier = applicationContext.getBean(AliyunNoticeSupplier.class, ps);

        supplier.send(new To() {
            @Override
            public String mobilePhone() {
                return "18606509616";
            }

            @Override
            public Set<EmailAddress> emailTo() {
                return null;
            }
        }, new Content() {
            @Override
            public String asText() {
                return null;
            }

            @Override
            public List<DataSource> embedAttachments() {
                return null;
            }

            @Override
            public List<DataSource> otherAttachments() {
                return null;
            }

            @Override
            public String asHtml(Map<String, String> attachmentRefs) {
                return null;
            }

            @Override
            public String asTitle() {
                return null;
            }

            @Override
            public String signName() {
                return "蒋才所用";
            }

            @Override
            public String templateName() {
                return "SMS_94740068";
            }

            @Override
            public Map<String, ?> templateParameters() {
                HashMap<String, String> data = new HashMap<>();
                data.put("code", "love");
                return data;
            }
        });
    }


//    @Configuration
//    @PropertySource("classpath:/local_aliyun.properties")
//    public static class Config {
//    }
    // LTAI5I2jZhp826XC
    // moBAzTN6cKOD0aBpwNHyOXb1iFWrF0
    // 属于我个人阿里云账户的sms用户 可以开放

}