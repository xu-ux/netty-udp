package com.github.xucux.udp.single.handler;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @descriptions:
 * @author: xucl
 * @date: 2021/12/2 9:45
 * @version: 1.0
 */
@Slf4j
public class UdpAnswerHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    private static String ANSWER_STR = "你好长江，我是黄河，已收到信息，Over!";

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
        //获取发送方的udp报文数据
        String content = msg.content().toString(CharsetUtil.UTF_8);
        log.info("[接收方] <<<< 收到应答信息为:【{}】",content);
        //应答,由于UDP报文头是包含了目的端口和源端口号，所以可以直接通过DatagramPacket中获得 [发送方] 的信息
        ctx.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(ANSWER_STR,CharsetUtil.UTF_8), msg.sender()));
        log.info("[接收方] >>>> 发送应答信息为:【{}】",ANSWER_STR);
    }
}
