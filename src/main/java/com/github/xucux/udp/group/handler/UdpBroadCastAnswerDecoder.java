package com.github.xucux.udp.group.handler;

import com.github.xucux.model.LogEvent;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.CharsetUtil;

import java.util.List;

/**
 * @descriptions: 定义广播订阅方（接收方）的解码器，负责将收到的广播报文进行解码，解码成日志实例并传给后续业务handler进行处理
 * @author: xucl
 * @date: 2021/12/2 10:36
 * @version: 1.0
 */
public class UdpBroadCastAnswerDecoder extends MessageToMessageDecoder<DatagramPacket> {

    @Override
    protected void decode(ChannelHandlerContext ctx, DatagramPacket msg, List<Object> out) throws Exception {
        ByteBuf data= msg.content();
        //获得消息id
        long msgId = data.readLong();
        //消息发送时间
        long time = data.readLong();
        //分隔符
        byte sep = data.readByte();
        //获得消息内容
        //先获得分隔符的读索引位置，然后从该索引位置开始读，之后的才是消息内容
        String msgContent = data.slice(data.readerIndex(), data.readableBytes()).toString(CharsetUtil.UTF_8);
        //构造LogEvent
        LogEvent logEvent = new LogEvent(msg.sender(), msgContent, msgId, time);
        //解码成功，将logEvent向后面的handler传递
        out.add(logEvent);
    }
}

