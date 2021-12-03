package com.github.xucux.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.net.InetSocketAddress;

/**
 * @descriptions: 日志实体
 * @author: xucl
 * @date: 2021/12/2 10:28
 * @version: 1.0
 */
@Getter
@Setter
@ToString
public class LogEvent {

    //分隔符
    public static final byte SEPARATOR = (byte) ':';

    private InetSocketAddress source;

    //消息内容
    private String msg;

    //消息id
    private long msgId;

    //消息发送时间
    private long time;

    //用于传入消息的构造函数
    public LogEvent(InetSocketAddress source,String msg) {
        this(source, msg,-1,System.currentTimeMillis());
    }

    //用于传出消息的构造函数
    public LogEvent(InetSocketAddress source, long msgId,
                    String msg) {
        this(source,msg,msgId,System.currentTimeMillis());
    }

    public LogEvent(InetSocketAddress source, String msg, long msgId, long time) {
        this.source = source;
        this.msg = msg;
        this.msgId = msgId;
        this.time = time;
    }

}
