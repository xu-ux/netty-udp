package com.github.xucux.udp.group;

import com.github.xucux.model.LogEvent;
import com.github.xucux.model.LogEventConst;
import com.github.xucux.udp.group.handler.UdpBroadCastQuestionEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * @descriptions: 广播发送方
 * @author: xucl
 * @date: 2021/12/2 10:30
 * @version: 1.0
 */
@Slf4j
public class UdpBroadCastQuestionServer {

    private static final int PORT = 8761;

    private static final String HOST = "255.255.255.255";

    private static EventLoopGroup eventLoopGroup =new NioEventLoopGroup();

    private static Bootstrap bootstrap = new Bootstrap();

    public static void main(String[] args) {
        start();
    }

    public static void start(){
        try {
            bootstrap.group(eventLoopGroup)
                    //指定UDP传输方式
                    .channel(NioDatagramChannel.class)
                    //设置开启广播
                    .option(ChannelOption.SO_BROADCAST, true)
                    //设置编码器
                    .handler(new UdpBroadCastQuestionEncoder());
            //由于UDP是无连接的，因此不需要建立连接
            Channel channel= bootstrap.bind(0).sync().channel();
            InetSocketAddress inetSocketAddress =new InetSocketAddress(HOST, PORT);
            log.info("广播给 post={} 的服务器",PORT);
            int count = 0;
            while(true){
                channel.writeAndFlush(new LogEvent(inetSocketAddress, ++count, LogEventConst.getLogInfo()));
                Thread.sleep(3000);
            }
        } catch (Exception e) {
            log.error("UdpBroadCastQuestionServer 异常",e);
        }finally{
            eventLoopGroup.shutdownGracefully();
        }
    }


}
