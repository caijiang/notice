package me.jiangcai.lib.notice;

import me.jiangcai.lib.notice.service.NoticeServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author CJ
 */
@Configuration
@ComponentScan("me.jiangcai.lib.notice.supplier")
public class NoticeSpringConfig {

//    @Autowired
//    private ApplicationContext applicationContext;

    @Bean
    public NoticeService noticeService() {
        return new NoticeServiceImpl();
    }


}
