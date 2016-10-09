package me.jiangcai.lib.notice.locate;

import me.jiangcai.lib.notice.LocateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author CJ
 */
@Service
public class ZjyLocateService implements LocateService {

    @Autowired
    private ZjySubNoticeService zjySubNoticeService;

    @Override
    public ZjySubNoticeService locate(String locator) {
        if (locator.equalsIgnoreCase("zjy://") && zjySubNoticeService.working())
            return zjySubNoticeService;
        return null;
    }
}
