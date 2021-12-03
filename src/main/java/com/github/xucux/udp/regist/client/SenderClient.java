package com.github.xucux.udp.regist.client;

import com.github.xucux.model.MsgType;
import com.github.xucux.udp.regist.client.handler.SenderHandler;
import com.github.xucux.udp.regist.common.Convert;
import com.github.xucux.udp.regist.common.Encoder;
import com.github.xucux.udp.regist.server.service.ServerCache;
import com.github.xucux.util.IPUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.net.InetSocketAddress;

/**
 * @descriptions:
 * @author: xucl
 * @date: 2021/12/2 12:48
 * @version: 1.0
 */
@Slf4j
public class SenderClient {

    private EventLoopGroup eventLoopGroup;

    private Bootstrap bootstrap;

    private Channel channel;

    private static class Singleton {
        static final SenderClient instance = new SenderClient();
    }

    public static SenderClient getInstance() {
        return SenderClient.Singleton.instance;
    }

    public SenderClient(){
        eventLoopGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                //指定UDP传输方式
                .channel(NioDatagramChannel.class)
                .handler(new ChannelInitializer<DatagramChannel>() {
                    @Override
                    protected void initChannel(DatagramChannel ch) throws Exception {
                        //先添加解码器
                        ch.pipeline().addLast(new Encoder());
                        //添加业务handler
                        ch.pipeline().addLast(new SenderHandler());
                    }
                });
    }

    public void start(){
        try {
            // 由于UDP是无连接的，因此不需要建立连接
            channel = bootstrap.bind(0).sync().channel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void send(String msg){
        try {
            String serverInfo = ServerCache.getServerInfo();
            if (StringUtils.isEmpty(serverInfo)){
                log.info("传输数据发送失败，服务端信息未获取");
                // 启动后检查ServerCache
                if (StringUtils.isEmpty(ServerCache.getServerInfo())){
                    // 如果缓存没有数据，则发起广播，告知自己需要服务端信息
                    log.info("发起广播，告知自己需要服务端信息");
                    BroadSenderClient.getInstance().send(MsgType.NEED_SERVER_INFO, IPUtils.getHostIp());
                }
                return;
            }
            String[] info = serverInfo.split(":");
            // 将请求的UDP报文打包成DatagramPacket发送到接收方
            ByteBuf buffer = Convert.build(msg, MsgType.LOG_INFO);
            channel.writeAndFlush(new DatagramPacket(buffer,new InetSocketAddress(info[0], Integer.parseInt(info[1])))).sync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}
