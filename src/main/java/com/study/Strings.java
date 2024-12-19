package com.study;

import java.util.List;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;

public class Strings {

    public static void main(String[] args) {
        System.out.println("Hello world!");

        try (var jedisPool = new JedisPool("127.0.0.1", 6379)) {
            try(Jedis jedis = jedisPool.getResource()) {
                jedis.set("users:100:name", "hello");
                jedis.set("users:100:email", "hello@gmail.com");
                jedis.set("users:100:age", "20");

                String userName = jedis.get("users:100:name");
                System.out.println(userName);

                List<String> users = jedis.mget("users:100:name", "users:100:email", "users:100:age");
                System.out.println(users.toString());

//                long count; = jedis.incr("count");
//                System.out.println(count);
//
//                count = jedis.incrBy("count", 10);
//                System.out.println(count);
//
//                count = jedis.incr("count");
//
//                count = jedis.decr("count");
//                System.out.println(count);

                long count = jedis.decrBy("count", 10);
                System.out.println(count);

                System.out.println("------");

                // 묶어서 한번에
                Pipeline pipeline = jedis.pipelined();
                pipeline.set("users:200:name", "test");
                pipeline.set("users:200:email", "test@gmail.com");
                pipeline.set("users:200:age", "24");
                List<Object> objects = pipeline.syncAndReturnAll();
                System.out.println(objects.toString()); // OK 반환됨
                // 1734609865.069670 [0 172.17.0.1:62250] "SET" "users:200:name" "test"
                // 1734609865.069682 [0 172.17.0.1:62250] "SET" "users:200:email" "test@gmail.com"
                // 1734609865.069761 [0 172.17.0.1:62250] "SET" "users:200:age" "24"

            }
        }

    }
}