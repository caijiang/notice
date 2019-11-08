package me.jiangcai.lib.notice.std;

import me.jiangcai.lib.notice.Content;
import me.jiangcai.lib.notice.NoticeSupplier;
import me.jiangcai.lib.notice.To;
import me.jiangcai.lib.notice.exception.NoticeException;

import java.io.IOException;

/**
 * @author CJ
 */
public class StdNoticeSupplier implements NoticeSupplier {
    @Override
    public void send(To to, Content content) throws NoticeException {
        System.out.println(" sending " + content + " to " + to);
    }

    @Override
    public void statusReport() throws IOException {

    }
}
