package com.github.xucux.udp.regist.server.service;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * @descriptions:
 * @author: xucl
 * @date: 2021/12/2 13:38
 * @version: 1.0
 */
@Slf4j
public class ServerCache {

    public static final String SERVER_INFO = "SERVER_INFO";

    private static ConcurrentHashMap<String,String> concurrentHashMap = new ConcurrentHashMap<>();


    private static ConcurrentSkipListSet<String> serverInfo = new ConcurrentSkipListSet<String>();

    public static void add(String key,String data){
        concurrentHashMap.put(key, data);
    }

    public static String get(String key){
       return concurrentHashMap.get(key);
    }

    public static void addServerInfo(String data){
        log.info("server add server info :{}",data);
        serverInfo.add(data);
    }

    public static String getServerInfo(){
        if (serverInfo.isEmpty()){
            return null;
        }
        return serverInfo.last();
    }
}
