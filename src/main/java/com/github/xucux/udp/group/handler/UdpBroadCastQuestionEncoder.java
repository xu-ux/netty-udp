package com.github.xucux.udp.group.handler;

import com.github.xucux.model.LogEvent;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * @descriptions:
 * @author: xucl
 * @date: 2021/12/2 10:32
 * @version: 1.0
 */
public class UdpBroadCastQuestionEncoder extends MessageToMessageEncoder<LogEvent> {

    @Override
    protected void encode(ChannelHandlerContext ctx, LogEvent msg, List<Object> out) throws Exception {
        //获得日志内容
        byte[] msgByte= msg.getMsg().getBytes();
        //创建ByteBuf(2个long类型+消息内容的字节长度+1一个分隔符字节)
        ByteBuf byteBuf = ctx.alloc().buffer(8*2+msgByte.length+1);
        //将信息写入到byteBuf中
        byteBuf.writeLong(msg.getMsgId());
        byteBuf.writeLong(msg.getTime());
        byteBuf.writeByte(LogEvent.SEPARATOR);
        byteBuf.writeBytes(msgByte);
        //向后续handler传递
        out.add(new DatagramPacket(byteBuf,msg.getSource()));
    }
}
