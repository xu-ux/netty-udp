package com.github.xucux.udp.group;

import com.github.xucux.udp.group.handler.UdpBroadCastAnswerDecoder;
import com.github.xucux.udp.group.handler.UdpBroadCastAnswerHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * @descriptions: 广播订阅方
 * @author: xucl
 * @date: 2021/12/2 10:34
 * @version: 1.0
 */
@Slf4j
public class UdpBroadCastAnswerServer {

    private static final int PORT = 8761;

    private static EventLoopGroup eventLoopGroup =new NioEventLoopGroup();

    //由于UDP是无连接的，因此接收方也是使用Bootstrap
    private static Bootstrap bootstrap = new Bootstrap();

    public static void main(String[] args) {
        startAnswer();
    }

    public static void startAnswer(){
        try {
            bootstrap.group(eventLoopGroup)
                    //指定UDP传输方式
                    .channel(NioDatagramChannel.class)
                    //设置广播
                    .option(ChannelOption.SO_BROADCAST, true)
                    //端口复用
                    .option(ChannelOption.SO_REUSEADDR, true)
                    .handler(new ChannelInitializer<DatagramChannel>() {

                        @Override
                        protected void initChannel(DatagramChannel ch) throws Exception {
                            //先添加解码器
                            ch.pipeline().addLast(new UdpBroadCastAnswerDecoder());
                            //添加业务handler
                            ch.pipeline().addLast(new UdpBroadCastAnswerHandler());
                        }
                    });
            //没有接受客户端连接的过程，监听本地端口即可
            ChannelFuture future=bootstrap.bind(PORT).sync();
            log.info("启动应答广播服务.....，监听端口：{}",PORT);
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error("UdpBroadCastAnswerServer 异常",e);
        } finally{
            eventLoopGroup.shutdownGracefully();
        }
    }


}
