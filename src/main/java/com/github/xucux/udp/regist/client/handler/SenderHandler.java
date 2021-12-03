package com.github.xucux.udp.regist.client.handler;

import com.github.xucux.model.TransmitData;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @descriptions:
 * @author: xucl
 * @date: 2021/12/2 15:03
 * @version: 1.0
 */
@Slf4j
public class SenderHandler extends SimpleChannelInboundHandler<TransmitData> {

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,
                                Throwable cause) throws Exception {
        //当异常发生时，打印栈跟踪信息，并关闭对应的 Channel
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TransmitData data) throws Exception {
        // 获取应答信息
        log.info("[发送方] <<<< 收到应答信息为:{}",data.toString());
    }

}
