package com.github.xucux.model;

import java.util.Arrays;

/**
 * @descriptions:
 * @author: xucl
 * @date: 2021/12/2 11:27
 * @version: 1.0
 */
public enum MsgType {

    NEED_SERVER_INFO(0,"需要知道服务器的IP和端口"),
    SERVER_INFO(1,"告知自己知道的服务器的IP和端口"),
    LOG_INFO(2,"传输日志信息"),
    RECEIVE(3,"收到消息的应答"),
    ;

    private int msgId;

    private String name;

    MsgType(int msgId, String name) {
        this.msgId = msgId;
        this.name = name;
    }

    public static MsgType idOf(int id){
        return Arrays.asList(MsgType.values()).stream().filter(s -> s.getMsgId() == id).findFirst().orElse(null);
    }

    public int getMsgId() {
        return msgId;
    }

    public String getName() {
        return name;
    }
}
