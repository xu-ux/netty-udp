package com.github.xucux.udp.regist.client;

import com.github.xucux.udp.regist.client.handler.BroadSubHandler;
import com.github.xucux.udp.regist.common.Decoder;
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
public class BroadSubClient implements Runnable{

    private static final int PORT = 52098;

    private EventLoopGroup eventLoopGroup;

    private Bootstrap bootstrap;

    private ChannelFuture channelFuture;

    private static class Singleton {
        static final BroadSubClient instance = new BroadSubClient();
    }

    public static BroadSubClient getInstance() {
        return BroadSubClient.Singleton.instance;
    }

    public BroadSubClient(){
        eventLoopGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
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
                        ch.pipeline().addLast(new Decoder());
                        //添加业务handler
                        ch.pipeline().addLast(new BroadSubHandler());
                    }
                });
    }

    @Override
    public void run(){
        try {
            channelFuture = bootstrap.bind(PORT).sync();
            log.info("启动广播订阅服务-Client-.....，监听端口：{}",PORT);
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error("UdpBroadSubServer 异常",e);
        } finally {
            eventLoopGroup.shutdownGracefully();
        }
    }

}
