package com.study;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class Set {

    public static void main(String[] args) {
        try (var jedisPool = new JedisPool("127.0.0.1", 6379)) {
            try (Jedis jedis = jedisPool.getResource()) {

                System.out.println(jedis.sadd("users:30:follow", "130", "110", "11"));
                // 3, "SADD" "users:30:follow" "130" "110" "11"

                System.out.println(jedis.srem("users:30:follow", "130"));
                // 1, "SREM" "users:30:follow" "130"

                System.out.println(jedis.smembers("users:30:follow"));
                // [11, 110], "SMEMBERS" "users:30:follow"

                jedis.sadd("users:30:follow", "130");
                // 터미널에서 130 추가해줬던 key가 있어서 테스트를 위해 삭제했던 130 추가

                System.out.println(jedis.sismember("users:30:follow", "130")); // true
                System.out.println(jedis.sismember("users:30:follow", "1"));   // false

                System.out.println(jedis.scard("users:30:follow")); // 3
                System.out.println(jedis.sinter("users:30:follow", "users:100:follow")); // [130]
            }
        }
    }

}
