package com.github.xucux.udp.regist.server;

import com.github.xucux.model.MsgType;
import com.github.xucux.udp.regist.common.Decoder;
import com.github.xucux.udp.regist.server.handler.SubHandler;
import com.github.xucux.udp.regist.server.service.ServerCache;
import com.github.xucux.util.IPUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @descriptions: 单播应答服务端
 * @author: xucl
 * @date: 2021/12/2 9:42
 * @version: 1.0
 */
@Slf4j
public class SubServer{

    private EventLoopGroup eventLoopGroup;

    private Bootstrap bootstrap;

    private static class Singleton {
        static final SubServer instance = new SubServer();
    }

    public static SubServer getInstance() {
        return SubServer.Singleton.instance;
    }


    public SubServer(){
        eventLoopGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                //指定UDP传输方式
                .channel(NioDatagramChannel.class)
                .handler(new ChannelInitializer<DatagramChannel>() {

                    @Override
                    protected void initChannel(DatagramChannel ch) throws Exception {
                        //先添加解码器
                        ch.pipeline().addLast(new Decoder());
                        //添加业务handler
                        ch.pipeline().addLast(new SubHandler());
                    }
                });
        //没有接受客户端连接的过程，监听本地端口即可
    }


    public static ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(1);

    public void start(int port){
        try {
//            int port = FreePort.build().getPortAndRelease();
            ChannelFuture future = bootstrap.bind(port).sync();
            log.info("启动单播订阅服务，监听端口：{}",port);
            ServerCache.addServerInfo(IPUtils.getHostIp().concat(":").concat(String.valueOf(port)));
            // 单播订阅启动的同时，广播全网服务器，告知自己的单播订阅信息
            scheduledThreadPool.scheduleAtFixedRate(()->{
                BroadSenderServer.getInstance().send(MsgType.SERVER_INFO, ServerCache.getServerInfo());
            }, 0,10, TimeUnit.MINUTES);
            future.channel().closeFuture().sync();
//            log.info("单播订阅服务停止");
        } catch (Exception e) {
            log.error("SubServer 异常",e);
        }
    }

    public void close(){
        eventLoopGroup.shutdownGracefully();
        scheduledThreadPool.shutdown();
    }


}
