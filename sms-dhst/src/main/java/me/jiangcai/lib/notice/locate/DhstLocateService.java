package me.jiangcai.lib.notice.locate;

import me.jiangcai.lib.notice.LocateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author CJ
 */
@Service
public class DhstLocateService implements LocateService {

    @Autowired
    private DhstSubNoticeService dhstSubNoticeService;

    @Override
    public DhstSubNoticeService locate(String locator) {
        if (locator.equalsIgnoreCase("dhst://"))
            return dhstSubNoticeService;
        return null;
    }
}
