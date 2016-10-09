package me.jiangcai.lib.notice;

import lombok.Data;

/**
 * @author CJ
 */
@Data
public class StatusReport {

    private final SubNoticeService subNoticeService;
    private final Object data;

}
