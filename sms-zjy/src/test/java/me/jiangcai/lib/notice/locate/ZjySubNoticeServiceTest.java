package me.jiangcai.lib.notice.locate;

import me.jiangcai.lib.notice.Content;
import me.jiangcai.lib.notice.NoticeService;
import me.jiangcai.lib.notice.NoticeSpringConfig;
import me.jiangcai.lib.notice.StatusReport;
import me.jiangcai.lib.notice.To;
import me.jiangcai.lib.notice.dhst.Fee;
import me.jiangcai.lib.test.SpringWebTest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.EventListener;
import org.springframework.test.context.ContextConfiguration;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author CJ
 */
@ContextConfiguration(classes = {ZjySubNoticeServiceTest.Config.class, NoticeSpringConfig.class})
public class ZjySubNoticeServiceTest extends SpringWebTest {

    private static final Log log = LogFactory.getLog(ZjySubNoticeServiceTest.class);

    private static Fee fee;
    @Autowired
    private NoticeService noticeService;

    @Test
    public void hello() {
        //  //账号：meiyueyzm  密码：meiyue6629     请提交正常短信内容（验证码类），建议不要出现测试等字样，签名是平台自动加的
//        noticeService.send("zjy://", new To() {
//            @Override
//            public String mobilePhone() {
//                return "18606509616";
//            }
//        }, new Content() {
//            @Override
//            public String asText() {
//                return "数字1，汉字，你的验证码是："+ UUID.randomUUID().toString().substring(0,5);
//            }
//        });

        noticeService.send("zjy://", new To() {
            @Override
            public String mobilePhone() {
                return "18606509616";
            }
        }, new Content() {
            @Override
            public String asText() {
                // 【款爷二手】
                return "您好，您的验证码是" + UUID.randomUUID().toString().substring(0, 5) + "，请勿泄露给他人。";
            }
        });

//        【美约科技】亲爱的会员您好，您的注册验证码是123456，请勿泄露给他人。


        noticeService.status("zjy://");
        assertThat(fee)
                .isNotNull();
        System.out.println(fee);
    }

    @Configuration
    @PropertySource("classpath:/meiyue.properties")
    static class Config {
        @EventListener
        public void status(StatusReport statusReport) {
            fee = (Fee) statusReport.getData();
            log.debug(statusReport);
        }
    }

}