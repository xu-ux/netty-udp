package com.github.xucux.udp.single;

import com.github.xucux.model.TransmitData;
import com.github.xucux.udp.single.handler.UdpQuestionHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * @descriptions: 单播发送方
 * @author: xucl
 * @date: 2021/12/2 9:38
 * @version: 1.0
 */
@Slf4j
public class UdpQuestionServer {

    private static final int PORT = 8761;

    /**
     * 发送目标地址
     */
    private static final String HOST = "127.0.0.1";

    private static String QUESTION_STR = "你好黄河，这里是长江，收到请回答，Over!";

    private static EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

    private static Bootstrap bootstrap = new Bootstrap();


    public static void main(String[] args) {
        startQuestion();
    }

    public static void startQuestion(){
        try {
            bootstrap.group(eventLoopGroup)
                    //指定UDP传输方式
                    .channel(NioDatagramChannel.class)
                    .handler(new UdpQuestionHandler());
            // 由于UDP是无连接的，因此不需要建立连接
            Channel channel= bootstrap.bind(0).sync().channel();
            // 将请求的UDP报文打包成DatagramPacket发送到接收方
            channel.writeAndFlush(
                    new DatagramPacket(Unpooled.copiedBuffer(QUESTION_STR, CharsetUtil.UTF_8),
                            new InetSocketAddress(HOST, PORT))).sync();
//            channel.writeAndFlush(
//                    new DatagramPacket(convert("测试", MsgType.LOG_INFO.getMsgId()),
//                            new InetSocketAddress(HOST, 55769))).sync();
            //由于不确定接收方是否能够收到报文，并且当前能否收到应答报文也不确定
            //因此需要为channel设置等待10s,超时10s就关闭连接
            if(channel.closeFuture().await(10000)){
                log.info("超时会话，关闭连接");
            }
        } catch (Exception e) {
            log.error("UdpQuestionServer 异常",e);
        } finally{
            eventLoopGroup.shutdownGracefully();
        }
    }


    public static ByteBuf convert(String msg, int type){
        byte[] msgByte= msg.getBytes();
        //创建ByteBuf(1个int1个long类型+消息内容的字节长度+1一个分隔符字节)
        ByteBuf buffer = Unpooled.buffer(4 + 8 + msgByte.length + 1);
        //将信息写入到byteBuf中
        buffer.writeInt(type);
        buffer.writeLong(System.currentTimeMillis());
        buffer.writeByte(TransmitData.SEPARATOR);
        buffer.writeBytes(msgByte);
        return buffer;
    }


}
