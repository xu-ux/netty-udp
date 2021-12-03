package com.github.xucux.model;

import java.util.Random;

/**
 * @descriptions:
 * @author: xucl
 * @date: 2021/12/2 10:29
 * @version: 1.0
 */
public class LogEventConst {

//    public final static int MONITOR_SIDE_PORT = 9998;


    private final static Random r = new Random();

    private static final String[] LOG_INFOS = {
            "20180912:admin:Send sms to 10001",
            "20180912:user1:Send email to 123@qq.com",
            "20180912:user2:Happen Exception",
            "20180912:user3:Send email to 1234567@qq.com", };

    public static String getLogInfo(){
        return LOG_INFOS[r.nextInt(LOG_INFOS.length-1)];
    }
}
