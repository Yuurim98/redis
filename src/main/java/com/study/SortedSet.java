package com.study;

import java.util.HashMap;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class SortedSet {

    public static void main(String[] args) {
        try (var jedisPool = new JedisPool("127.0.0.1", 6379)) {
            try (Jedis jedis = jedisPool.getResource()) {

                var scores = new HashMap<String, Double>();
                scores.put("user1", 100.0);
                scores.put("user2", 90.0);
                scores.put("user3", 80.0);
                scores.put("user4", 70.0);
                scores.put("user5", 65.0);
                scores.put("user6", 11.0);

                jedis.zadd("game2:scores", scores); // 단건도 가능
                // "ZADD" "game2:scores" "100.0" "user1" "90.0" "user2" "65.0" "user5" "11.0" "user6" "80.0" "user3" "70.0" "user4"

                System.out.println(jedis.zrange("game2:scores", 0, Long.MAX_VALUE));
                // [user6, user5, user4, user3, user2, user1] 기본 오름차순

                System.out.println(jedis.zrangeWithScores("game2:scores", 0, Long.MAX_VALUE));
                // [[user6,11.0], [user5,65.0], [user4,70.0], [user3,80.0], [user2,90.0], [user1,100.0]]
                // List<Tuple>

                System.out.println(jedis.zcard("game2:scores")); // 6

                jedis.zincrby("game2:scores", 70.0, "user6");

                System.out.println(jedis.zrangeWithScores("game2:scores", 0, Long.MAX_VALUE));
                // [[user5,65.0], [user4,70.0], [user3,80.0], [user6,81.0], [user2,90.0], [user1,100.0]]


            }
        }
    }

}
