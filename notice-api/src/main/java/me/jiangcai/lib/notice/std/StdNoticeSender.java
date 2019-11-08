package me.jiangcai.lib.notice.std;

import me.jiangcai.lib.notice.NoticeSender;
import me.jiangcai.lib.notice.NoticeSupplier;

/**
 * @author CJ
 */
public class StdNoticeSender implements NoticeSender {
    @Override
    public NoticeSupplier toNoticeSupplier() {
        return new StdNoticeSupplier();
    }
}
