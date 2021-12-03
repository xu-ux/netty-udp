package com.github.xucux.udp.regist.common;

import com.github.xucux.model.MsgType;
import com.github.xucux.model.TransmitData;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @descriptions:
 * @author: xucl
 * @date: 2021/12/3 9:49
 * @version: 1.0
 */
public class Convert {

    public static ByteBuf build(String msg, MsgType msgType){
        byte[] msgByte= msg.getBytes();
        //创建ByteBuf(1个int1个long类型+消息内容的字节长度+1一个分隔符字节)
        ByteBuf buffer = Unpooled.buffer(4 + 8 + msgByte.length + 1);
        //将信息写入到byteBuf中
        buffer.writeInt(msgType.getMsgId());
        buffer.writeLong(System.currentTimeMillis());
        buffer.writeByte(TransmitData.SEPARATOR);
        buffer.writeBytes(msgByte);
        return buffer;
    }
}
