
package com.github.xucux.udp.single;


import com.github.xucux.udp.single.handler.UdpQuestionHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Scanner;

/**
 * @descriptions:
 * @author: xucl
 * @date: 2021/12/2 12:48
 * @version: 1.0
 */
@Slf4j
public class UdpQuestionServer2 {

    private EventLoopGroup eventLoopGroup;

    private Bootstrap bootstrap;

    private Channel channel;

    private static final int PORT = 8761;

    /**
     * 发送目标地址
     */
    private static final String HOST = "127.0.0.1";


    private static class Singleton {
        static final UdpQuestionServer2 instance = new UdpQuestionServer2();
    }

    public static UdpQuestionServer2 getInstance() {
        return Singleton.instance;
    }

    public UdpQuestionServer2(){
        eventLoopGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                //指定UDP传输方式
                .channel(NioDatagramChannel.class)
                .option(ChannelOption.SO_RCVBUF, 2048 * 2048)
                .handler(new UdpQuestionHandler());
    }

    public void start(){
        try {
            // 由于UDP是无连接的，因此不需要建立连接
            channel = bootstrap.bind(0).sync().channel();
            log.info("Remote Server Settings Udp IP:{} Port:{} ",HOST,PORT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void send(String msg){
        try {
            // 将请求的UDP报文打包成DatagramPacket发送到接收方
            channel.writeAndFlush(
                    new DatagramPacket(Unpooled.copiedBuffer(msg, CharsetUtil.UTF_8),
                            new InetSocketAddress(HOST, PORT))).sync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args){
        UdpQuestionServer2.getInstance().start();
        while (true){
            System.out.println("请输入内容：");
            Scanner input = new Scanner(System.in);
            String str = input.next();
            UdpQuestionServer2.getInstance().send(str);
        }
    }



}
