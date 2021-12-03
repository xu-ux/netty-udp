package com.github.xucux.udp.regist.server;

import com.github.xucux.model.MsgType;
import com.github.xucux.model.TransmitData;
import com.github.xucux.udp.regist.common.Encoder;
import com.github.xucux.util.BroadIpUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * @descriptions: 广播信息发送
 * @author: xucl
 * @date: 2021/12/2 11:53
 * @version: 1.0
 */
@Slf4j
public class BroadSenderServer implements Runnable{


    private static final int PORT = 52098;

    /** 受限广播/本地广播地址 */
    private static final String HOST = "255.255.255.255";

    private EventLoopGroup eventLoopGroup;

    private Bootstrap bootstrap;

    private Channel channel;

    private static class Singleton {
        static final BroadSenderServer instance = new BroadSenderServer();
    }

    public static BroadSenderServer getInstance() {
        return BroadSenderServer.Singleton.instance;
    }


    public BroadSenderServer(){
        eventLoopGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                //指定UDP传输方式
                .channel(NioDatagramChannel.class)
                //设置开启广播
                .option(ChannelOption.SO_BROADCAST, true)
                //设置编码器
                .handler(new Encoder());
    }

    public void close(){
        eventLoopGroup.shutdownGracefully();

    }

    @Override
    public void run(){
        try {
//            if (channel != null){
//                return;
//            }
            //由于UDP是无连接的，因此不需要建立连接
            channel = bootstrap.bind(0).sync().channel();

        } catch (InterruptedException e) {
            log.error("UdpBroadCastSenderSevServer 异常", e);
        }
    }

    public void send(MsgType msgType,String msg){
        try {
            InetSocketAddress local = new InetSocketAddress(HOST, PORT);
            String broadCast = BroadIpUtils.getLocalBroadCast();
            InetSocketAddress lan = new InetSocketAddress(broadCast, PORT);
            log.info("广播给 local={} lan={} post={} 的服务器",HOST,broadCast,PORT);
            channel.writeAndFlush(new TransmitData(local,msgType.getMsgId(),msg));
            channel.writeAndFlush(new TransmitData(lan,msgType.getMsgId(),msg));
        } catch (Exception e) {
            log.error("广播消息异常", e);
        }
    }
}
