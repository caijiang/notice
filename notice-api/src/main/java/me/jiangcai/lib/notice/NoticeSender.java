package me.jiangcai.lib.notice;

/**
 * 宿主，拥有以一个发送服务
 *
 * @author CJ
 */
public interface NoticeSender {

    NoticeSupplier toNoticeSupplier();
}
