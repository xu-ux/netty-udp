package com.github.xucux.udp.regist.server.handler;

import com.github.xucux.model.MsgType;
import com.github.xucux.model.TransmitData;
import com.github.xucux.udp.regist.server.BroadSenderServer;
import com.github.xucux.udp.regist.server.service.ServerCache;
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
public class BroadSubHandler extends SimpleChannelInboundHandler<TransmitData> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TransmitData data) throws Exception {
        log.info("[订阅方-Server] <<<< 收到广播消息：{}",data.toString());
        if (MsgType.NEED_SERVER_INFO.equals(data.getType())){
            BroadSenderServer.getInstance().send(MsgType.SERVER_INFO, ServerCache.getServerInfo());
            log.info("[订阅方-Server] >>>> 发起广播服务端单播信息：{}",ServerCache.getServerInfo());
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,
                                Throwable cause) throws Exception {
        //当异常发生时，打印栈跟踪信息，并关闭对应的 Channel
        cause.printStackTrace();
        ctx.close();
    }
}