package me.jiangcai.lib.notice;

import me.jiangcai.lib.notice.exception.NoticeException;

import java.io.IOException;

/**
 * 子服务
 *
 * @author CJ
 */
public interface SubNoticeService {

    void send(To to, Content content) throws NoticeException;

    void statusReport() throws IOException;
}
