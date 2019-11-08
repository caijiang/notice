package me.jiangcai.lib.notice;

import me.jiangcai.lib.notice.exception.NoticeException;

/**
 * 通知服务
 *
 * @author CJ
 */
public interface NoticeService {

    /**
     * 通过唯一供应商发送通知
     *
     * @param to      收件人
     * @param content 通知文本
     * @throws NoticeException       供应商异常
     * @throws IllegalStateException 目前存在多个供应商
     */
    void send(To to, Content content) throws NoticeException, IllegalStateException;

    /**
     * 通过服务定位器发送通知
     *
     * @param sender  发送者
     * @param to      收件人
     * @param content 内容
     * @throws NoticeException       供应商异常
     * @throws IllegalStateException 找不到供应商
     */
    void send(NoticeSender sender, To to, Content content) throws NoticeException, IllegalStateException, ClassNotFoundException;

    void status();

    void status(NoticeSender sender) throws ClassNotFoundException;

}
