package org.dsd;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {

    public static void main(String[] args) {
        try{
            RedisServer redisServer = new RedisServer(6380);
            redisServer.start();
        }
        catch(Exception e){
            log.error("Exception: ", e);
        }    
    }
}
