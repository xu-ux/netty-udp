package com.github.xucux.util;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Random;

/**
 * @descriptions: 获取空闲端口
 * @author: xucl
 * @date: 2021/12/2 9:29
 * @version: 1.0
 *
 * 0-1023: BSD保留端口,也叫系统端口,这些端口只有系统特许的进程才能使用;
 *
 * 1024-5000: BSD临时端口,一般的应用程序使用1024到4999来进行通讯;
 *
 * 5001-65535: BSD服务器(非特权)端口,用来给用户自定义端口.
 *
 *
 */
@Slf4j
public class FreePort {

    private static Random random = new Random();

    private Socket socket;


    public static FreePort build() throws IOException {
        return new FreePort();
    }

    /**
     * 当入参port为0时，由系统自动分配一个临时端口
     *
     * @throws IOException
     */
    private FreePort() throws IOException {
        socket = new Socket();

        InetSocketAddress inetAddress = new InetSocketAddress(0);

        socket.bind(inetAddress);

    }

    /**
     * 释放端口
     * @throws IOException
     */
    public void releasePort() throws IOException {
        if (null == socket || socket.isClosed()) {
            return;
        }
        socket.close();
    }

    /**
     * 获取端口
     * @return
     */
    public int getPort() {
        if (null == socket || socket.isClosed()) {
            return -1;
        }
        return socket.getLocalPort();
    }

    /**
     * 返回端口并释放该端口资源
     * @return
     * @throws IOException
     */
    public int getPortAndRelease() throws IOException{
        if (null == socket || socket.isClosed()) {
            return -1;
        }
        int port = socket.getLocalPort();
        socket.close();
        return port;
    }

    /**
     * 获取start-end之间的随机数
     * @param start
     * @param end
     * @return
     */
    public static int random(int start, int end) {
        return random.nextInt(Math.abs(end - start)) + start;
    }


    public static void main(String [] args) throws IOException {
        int portAndRelease = FreePort.build().getPortAndRelease();
        log.info("空闲端口：{}",portAndRelease);
    }

}
