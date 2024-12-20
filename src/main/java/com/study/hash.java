package com.study;

import java.util.HashMap;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class hash {

    public static void main(String[] args) {
        try (var jedisPool = new JedisPool("127.0.0.1", 6379)) {
            try (Jedis jedis = jedisPool.getResource()) {

               jedis.hset("users:3:info", "name", "test2"); // 단건
                // "HSET" "users:3:info" "name" "test2"

                var userInfo = new HashMap<String, String>();
                userInfo.put("email", "test2@gmail.com");
                userInfo.put("phone", "010-1111-1111");

                jedis.hset("users:3:info", userInfo); // 여러건
                // "HSET" "users:3:info" "phone" "010-1111-1111" "email" "test2@gmail.com"

                jedis.hdel("users:3:info", "phone"); // 여러 필드 가능
                // "HDEL" "users:3:info" "phone"

                System.out.println(jedis.hget("users:3:info", "name"));
                // test2

                System.out.println(jedis.hgetAll("users:3:info"));
                // {name=test2, email=test2@gmail.com}  Map<String, String>

                jedis.hincrBy("users:3:info", "visits", 1);

            }
        }
    }

}
