package com.github.xucux.util;

import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.List;

/**
 * @descriptions:
 * @author: xucl
 * @date: 2021/12/3 9:10
 * @version: 1.0
 */
public class BroadIpUtils {

    /**
     * 本地广播： 地址就是255.255.255.255，它不经路由转发，发送本地网络下的所有主机，只能在局域网内转发，主要是不用知道目标主机的掩码与网络地址，本地转发同本地网络下的所有主机。
     */
    public static String LOCAL_HOST = "255.255.255.255";

    /**
     * 直接广播： 计算方法通过主机的掩码与网络地址计算出来。掩码最后为0的位为主机位。掩码与网络地址相与，然后主机位全变为1，就是直接广播地址。这样该网络地址下的所有主机都能接收到广播。
     * 获取直接广播地址，并自动区分Windows还是Linux操作系统
     * @return String
     */
    public static String getLocalBroadCast(){
        String broadCastIp = null;
        try {
            Enumeration<?> netInterfaces = (Enumeration<?>) NetworkInterface.getNetworkInterfaces();
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = (NetworkInterface) netInterfaces.nextElement();
                if (!netInterface.isLoopback() && netInterface.isUp()) {
                    List<InterfaceAddress> interfaceAddresses = netInterface.getInterfaceAddresses();
                    for (InterfaceAddress interfaceAddress : interfaceAddresses) {
                        //只有 IPv4 网络具有广播地址，因此对于 IPv6 网络将返回 null。
                        if(interfaceAddress.getBroadcast() != null){
                            broadCastIp = interfaceAddress.getBroadcast().getHostAddress();

                        }
                    }
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return broadCastIp;
    }
}
