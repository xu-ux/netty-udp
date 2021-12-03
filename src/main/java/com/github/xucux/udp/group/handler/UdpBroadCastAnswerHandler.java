package com.github.xucux.udp.group.handler;

import com.github.xucux.model.LogEvent;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @descriptions: 接收方，业务handler
 * @author: xucl
 * @date: 2021/12/2 10:36
 * @version: 1.0
 */
@Slf4j
public class UdpBroadCastAnswerHandler extends SimpleChannelInboundHandler<LogEvent> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LogEvent msg) throws Exception {
        log.info("[订阅方] <<<< 收到广播消息：{}",msg.toString());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,
                                Throwable cause) throws Exception {
        //当异常发生时，打印栈跟踪信息，并关闭对应的 Channel
        cause.printStackTrace();
        ctx.close();
    }
}