package com.github.xucux.udp.regist.common;

import com.github.xucux.model.TransmitData;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * @descriptions:
 * @author: xucl
 * @date: 2021/12/2 11:56
 * @version: 1.0
 */
public class Encoder extends MessageToMessageEncoder<TransmitData> {

    @Override
    protected void encode(ChannelHandlerContext ctx, TransmitData data, List<Object> out) throws Exception {
        //获得日志内容
        byte[] msgByte= data.getMsg().getBytes();
        //创建ByteBuf(1个int1个long类型+消息内容的字节长度+1一个分隔符字节)
        ByteBuf byteBuf = ctx.alloc().buffer(4+8+msgByte.length+1);
        //将信息写入到byteBuf中
        byteBuf.writeInt(data.getMsgType());
        byteBuf.writeLong(data.getTime());
        byteBuf.writeByte(TransmitData.SEPARATOR);
        byteBuf.writeBytes(msgByte);
        //向后续handler传递
        out.add(new DatagramPacket(byteBuf,data.getSource()));
    }
}