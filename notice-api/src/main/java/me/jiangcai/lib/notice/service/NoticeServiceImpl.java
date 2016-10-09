package me.jiangcai.lib.notice.service;

import me.jiangcai.lib.notice.Content;
import me.jiangcai.lib.notice.LocateService;
import me.jiangcai.lib.notice.NoticeService;
import me.jiangcai.lib.notice.SubNoticeService;
import me.jiangcai.lib.notice.To;
import me.jiangcai.lib.notice.exception.NoticeException;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Set;

/**
 * @author CJ
 */
public class NoticeServiceImpl implements NoticeService {
    @Autowired
    private Set<LocateService> locateServiceSet;
    private SubNoticeService lastSubService;

    @Override
    public void send(To to, Content content) throws NoticeException, IllegalStateException {
        if (lastSubService == null)
            throw new IllegalStateException();
        doSend(lastSubService, to, content);
    }

    @Override
    public void send(String serviceLocator, To to, Content content) throws NoticeException, IllegalStateException {
        for (LocateService locateService : locateServiceSet) {
            SubNoticeService subNoticeService = locateService.locate(serviceLocator);
            if (subNoticeService != null) {
                lastSubService = subNoticeService;
                doSend(subNoticeService, to, content);
                return;
            }
        }
        throw new IllegalStateException("for " + serviceLocator);
    }

    @Override
    public void status() {
        if (lastSubService == null)
            throw new IllegalStateException();
        try {
            lastSubService.statusReport();
        } catch (IOException e) {
            throw new NoticeException(e);
        }
    }

    @Override
    public void status(String serviceLocator) {
        for (LocateService locateService : locateServiceSet) {
            SubNoticeService subNoticeService = locateService.locate(serviceLocator);
            if (subNoticeService != null) {
                lastSubService = subNoticeService;
                status();
                return;
            }
        }
    }

    private void doSend(SubNoticeService subNoticeService, To to, Content content) throws NoticeException {
        subNoticeService.send(to, content);
    }
}
