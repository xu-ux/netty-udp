package com.github.xucux.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.net.InetSocketAddress;

/**
 * @descriptions:
 * @author: xucl
 * @date: 2021/12/2 11:25
 * @version: 1.0
 */
@Getter
@Setter
@Accessors(chain = true)
@ToString
public class TransmitData {

    //分隔符
    public static final byte SEPARATOR = (byte) ':';

    private InetSocketAddress source;

    //消息内容
    private String msg;

    //消息类型
    private int msgType;

    //消息发送时间
    private long time;

    public MsgType getType(){
       return MsgType.idOf(msgType);
    }

    //用于传出消息的构造函数
    public TransmitData(InetSocketAddress source, int msgType,
                    String msg) {
        this(source,msg,msgType,System.currentTimeMillis());
    }

    public TransmitData(InetSocketAddress source, String msg, int msgType, long time) {
        this.source = source;
        this.msg = msg;
        this.msgType = msgType;
        this.time = time;
    }
}
