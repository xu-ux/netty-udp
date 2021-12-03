package com.github.xucux.udp.regist.server.handler;

import com.github.xucux.model.TransmitData;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @descriptions:
 * @author: xucl
 * @date: 2021/12/2 9:45
 * @version: 1.0
 */
@Slf4j
public class SubHandler extends SimpleChannelInboundHandler<TransmitData> {

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        super.exceptionCaught(ctx, cause);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx,TransmitData data) throws Exception {
        //获取发送方的udp报文数据
        log.info("[接收方] <<<< 收到应答信息为:【{}】",data.toString());
    }


}
