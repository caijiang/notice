package me.jiangcai.lib.notice.service;

import me.jiangcai.lib.notice.Content;
import me.jiangcai.lib.notice.NoticeSender;
import me.jiangcai.lib.notice.NoticeService;
import me.jiangcai.lib.notice.NoticeSupplier;
import me.jiangcai.lib.notice.To;
import me.jiangcai.lib.notice.exception.NoticeException;
import me.jiangcai.lib.notice.std.StdNoticeSender;

import java.io.IOException;

/**
 * @author CJ
 */
public class NoticeServiceImpl implements NoticeService {
//    private final ApplicationContext applicationContext;
//    private NoticeSupplier lastSupplier;

//    @Autowired
//    public NoticeServiceImpl(ApplicationContext applicationContext) {
//        this.applicationContext = applicationContext;
//    }

    @Override
    public void send(To to, Content content) throws NoticeException, IllegalStateException {
//        checkSupplier();
        send(new StdNoticeSender(), to, content);
    }

//    private void checkSupplier() {
//        if (lastSupplier == null) {
//            lastSupplier = applicationContext.getBean(NoticeSupplier.class);
//        }
//    }

    @Override
    public void send(NoticeSender sender, To to, Content content) throws NoticeException, IllegalStateException {
        doSend(sender.toNoticeSupplier(), to, content);
    }

//    private void checkSupplier(String clazz) throws ClassNotFoundException {
//        @SuppressWarnings("unchecked")
//        Class<? extends NoticeSupplier> type = (Class<? extends NoticeSupplier>) Class.forName(clazz);
//        lastSupplier = applicationContext.getBean(type);
//    }

    @Override
    public void status() {
        status(new StdNoticeSender());
    }

    @Override
    public void status(NoticeSender sender) {
        try {
            sender.toNoticeSupplier().statusReport();
        } catch (IOException e) {
            throw new NoticeException(e);
        }
    }

    private void doSend(NoticeSupplier noticeSupplier, To to, Content content) throws NoticeException {
        noticeSupplier.send(to, content);
    }
}
