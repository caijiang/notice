package me.jiangcai.lib.notice;

import me.jiangcai.lib.notice.exception.NoticeException;

import java.io.IOException;

/**
 * 短信服务供应商
 * 实现最好都在me.jiangcai.lib.notice.supplier包里
 *
 * @author CJ
 */
public interface NoticeSupplier {

    void send(To to, Content content) throws NoticeException;

    void statusReport() throws IOException;
}
