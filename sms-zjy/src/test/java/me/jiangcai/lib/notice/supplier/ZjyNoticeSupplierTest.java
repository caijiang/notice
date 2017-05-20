package me.jiangcai.lib.notice.supplier;

import me.jiangcai.lib.notice.Content;
import me.jiangcai.lib.notice.NoticeService;
import me.jiangcai.lib.notice.NoticeSpringConfig;
import me.jiangcai.lib.notice.StatusReport;
import me.jiangcai.lib.notice.To;
import me.jiangcai.lib.notice.zjy.Fee;
import me.jiangcai.lib.test.SpringWebTest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.EventListener;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author CJ
 */
@ContextConfiguration(classes = {ZjyNoticeSupplierTest.Config.class, NoticeSpringConfig.class})
public class ZjyNoticeSupplierTest extends SpringWebTest {

    private static final Log log = LogFactory.getLog(ZjyNoticeSupplierTest.class);

    private static Fee fee;
    @Autowired
    private NoticeService noticeService;

    @Test
    public void hello() throws ClassNotFoundException {
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

        // 文君 18381850466
        for (String number : new String[]{
                "18606509616"
//                "13588596617"
//                "15988289495", "15988207785", "13486582577", "15968517705", "15958525571", "18757552573"
        }) {
            noticeService.send("me.jiangcai.lib.notice.ZjyNoticeSupplier", new To() {
                @Override
                public String mobilePhone() {
//                return "18606509616";
//                    return "13588596617";
                    return number;
                }
            }, new Content() {
                @Override
                public String asText() {
                    // 【款爷二手】
//                return "您好，您的验证码是" + UUID.randomUUID().toString().substring(0, 5) + "，请勿泄露给他人。";
//                return "您的款爷帐号和所有申请都已经注销；如需继续使用款爷请重新注册。";
                    return "尊敬的文君，您在款爷网商宝的申请已撤销。祝您生活愉快";
                }
            });
        }


//        【美约科技】亲爱的会员您好，您的注册验证码是123456，请勿泄露给他人。


        noticeService.status("me.jiangcai.lib.notice.ZjyNoticeSupplier");
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