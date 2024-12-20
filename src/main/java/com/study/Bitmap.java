package com.study;

import java.util.stream.IntStream;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;

public class Bitmap {

    public static void main(String[] args) {
        try (var jedisPool = new JedisPool("127.0.0.1", 6379)) {
            try (Jedis jedis = jedisPool.getResource()) {
                jedis.setbit("request-somepage-20241221", 100, true);
                jedis.setbit("request-somepage-20241221", 300, true);
                jedis.setbit("request-somepage-20241221", 120, true);
                // "SETBIT" "request-somepage-20241221" "100" "1"
                // "SETBIT" "request-somepage-20241221" "300" "1"
                // "SETBIT" "request-somepage-20241221" "120" "1"

                System.out.println(jedis.getbit("request-somepage-20241221", 100)); // true
                System.out.println(jedis.getbit("request-somepage-20241221", 10)); // false

                System.out.println(jedis.bitcount("request-somepage-20241221")); // 3

                Pipeline pipeline = jedis.pipelined();
                IntStream.rangeClosed(0, 100000).forEach(i -> {
                    pipeline.sadd("request-somepage-set-20241222", String.valueOf(i), "1");
                    pipeline.setbit("request-somepage-bit-20241222", i, true);

                    if(i == 1000) {
                        pipeline.sync();
                    }
                });

                pipeline.sync();

                // set memory
                // 127.0.0.1:6379> MEMORY USAGE request-somepage-set-20241222
                // (integer) 4248736

                // bit memory
                // 127.0.0.1:6379> MEMORY USAGE request-somepage-bit-20241222
                // (integer) 16456
            }
        }
    }

}
