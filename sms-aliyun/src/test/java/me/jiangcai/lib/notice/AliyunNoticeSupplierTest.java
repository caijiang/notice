package me.jiangcai.lib.notice;

import me.jiangcai.lib.test.SpringWebTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author CJ
 */
@ContextConfiguration(classes = {AliyunNoticeSupplierTest.Config.class, NoticeSpringConfig.class})
public class AliyunNoticeSupplierTest extends SpringWebTest {

    @Autowired
    private NoticeService noticeService;

    @Test
    public void go() throws ClassNotFoundException {
        noticeService.send("me.jiangcai.lib.notice.AliyunNoticeSupplier", new To() {
            @Override
            public String mobilePhone() {
                return "18606509616";
            }
        }, new Content() {
            @Override
            public String asText() {
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


    @Configuration
    @PropertySource("classpath:/local_aliyun.properties")
    public static class Config {
    }
    // LTAI5I2jZhp826XC
    // moBAzTN6cKOD0aBpwNHyOXb1iFWrF0
    // 属于我个人阿里云账户的sms用户 可以开放

}