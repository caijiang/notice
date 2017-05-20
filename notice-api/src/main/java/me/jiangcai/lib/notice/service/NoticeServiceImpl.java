package me.jiangcai.lib.notice.service;

import me.jiangcai.lib.notice.Content;
import me.jiangcai.lib.notice.NoticeService;
import me.jiangcai.lib.notice.NoticeSupplier;
import me.jiangcai.lib.notice.To;
import me.jiangcai.lib.notice.exception.NoticeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.io.IOException;

/**
 * @author CJ
 */
public class NoticeServiceImpl implements NoticeService {
    private final ApplicationContext applicationContext;
    private NoticeSupplier lastSupplier;

    @Autowired
    public NoticeServiceImpl(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void send(To to, Content content) throws NoticeException, IllegalStateException {
        checkSupplier();

        doSend(lastSupplier, to, content);
    }

    private void checkSupplier() {
        if (lastSupplier == null) {
            lastSupplier = applicationContext.getBean(NoticeSupplier.class);
        }
    }

    @Override
    public void send(String supplierInterface, To to, Content content) throws NoticeException, IllegalStateException, ClassNotFoundException {
        checkSupplier(supplierInterface);
        send(to, content);
    }

    private void checkSupplier(String clazz) throws ClassNotFoundException {
        @SuppressWarnings("unchecked")
        Class<? extends NoticeSupplier> type = (Class<? extends NoticeSupplier>) Class.forName(clazz);
        lastSupplier = applicationContext.getBean(type);
    }

    @Override
    public void status() {
        checkSupplier();
        try {
            lastSupplier.statusReport();
        } catch (IOException e) {
            throw new NoticeException(e);
        }
    }

    @Override
    public void status(String supplierInterface) throws ClassNotFoundException {
        checkSupplier(supplierInterface);
        status();
    }

    private void doSend(NoticeSupplier noticeSupplier, To to, Content content) throws NoticeException {
        noticeSupplier.send(to, content);
    }
}
