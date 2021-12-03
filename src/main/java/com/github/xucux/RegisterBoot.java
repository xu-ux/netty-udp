package com.github.xucux;

import com.github.xucux.udp.regist.client.BroadSenderClient;
import com.github.xucux.udp.regist.client.BroadSubClient;
import com.github.xucux.udp.regist.client.SenderClient;
import com.github.xucux.udp.regist.server.BroadSenderServer;
import com.github.xucux.udp.regist.server.BroadSubServer;
import com.github.xucux.udp.regist.server.SubServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @descriptions:
 * @author: xucl
 * @date: 2021/12/2 11:43
 * @version: 1.0
 */
@Slf4j
@Component
public class RegisterBoot implements ApplicationListener<ContextRefreshedEvent> {


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null) {
            try {
                // 启动单播订阅服务
                new Thread(SubServer.getInstance()).start();
                // 启动广播订阅服务
                new Thread( BroadSubServer.getInstance()).start();
                new Thread(BroadSubClient.getInstance()).start();
                // 启动广播消息发送服务
                new Thread( BroadSenderServer.getInstance()).start();
                new Thread(BroadSenderClient.getInstance()).start();

                ExecutorService executorService = Executors.newFixedThreadPool(2);
                executorService.execute(()->{
                    SenderClient.getInstance().start();
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    SenderClient.getInstance().send("测试");
                });
            } catch (Exception e) {
                log.error("RegisterBoot Start Fail!",e);
            }
        }
    }

}

