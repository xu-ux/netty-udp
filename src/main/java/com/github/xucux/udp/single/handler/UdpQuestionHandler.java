package com.github.xucux.udp.single.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @descriptions: 发送方的业务Handler，用于接收应答方的应答报文
 * @author: xucl
 * @date: 2021/12/2 9:40
 * @version: 1.0
 */
@Slf4j
public class UdpQuestionHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
        // 获取应答信息
        String content = msg.content().toString(CharsetUtil.UTF_8);
        log.info("[发送方] <<<< 收到应答信息为:{}",content);
    }

}
