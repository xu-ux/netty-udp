package com.github.xucux.udp.single;

import com.github.xucux.udp.single.handler.UdpAnswerHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * @descriptions: 单播应答服务器
 * @author: xucl
 * @date: 2021/12/2 9:42
 * @version: 1.0
 */
@Slf4j
public class UdpAnswerServer {

    public static void main(String[] args) {
        startAnswer();
    }

    /**
     * 监听 0.0.0.0 的8761
     */
    private static final int PORT = 8761;

    private static EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

    /**
     * 由于UDP是无连接的，因此接收方也是使用Bootstrap
     */
    private static Bootstrap bootstrap = new Bootstrap();

    public static void startAnswer(){
        try {
            bootstrap.group(eventLoopGroup)
                    //指定UDP传输方式
                    .channel(NioDatagramChannel.class)
                    .handler(new UdpAnswerHandler());
            //没有接受客户端连接的过程，监听本地端口即可
            ChannelFuture future = bootstrap.bind(PORT).sync();
            log.info("启动应答服务，监听端口：{}",PORT);
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error("UdpAnswerServer 异常",e);
        } finally{
            eventLoopGroup.shutdownGracefully();
        }
    }


}
