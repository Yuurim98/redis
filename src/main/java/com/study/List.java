package com.study;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class List {

    public static void main(String[] args) {
        try (var jedisPool = new JedisPool("127.0.0.1", 6379)) {
            try(Jedis jedis = jedisPool.getResource()) {
                jedis.rpush("stack1", "a");
                jedis.rpush("stack1", "b");
                jedis.rpush("stack1", "c");

                 java.util.List<String> list = jedis.lrange("stack1", 0, -1);
                 System.out.println(list);

                // stack
                System.out.println(jedis.rpop("stack1")); // c
                System.out.println(jedis.rpop("stack1")); // b
                System.out.println(jedis.rpop("stack1")); // a

                jedis.rpush("queue", "a");
                jedis.rpush("queue", "b");
                jedis.rpush("queue", "c");

                System.out.println(jedis.lpop("queue")); // a
                System.out.println(jedis.lpop("queue")); // b
                System.out.println(jedis.lpop("queue")); // c

                // block - blpop, brpop
                java.util.List<String> blpop = jedis.blpop(10, "block:queue");
                // 리스트가 비어있으면 지정된 시간동안 추가될 때까지 대기 -> [block:queue, 100] 출력
                // 리스트에 값이 있으면 바로 반환
                // 0으로 설정하면 무한정 대기
                if(blpop != null) {
                    System.out.println(blpop);
                }

            }
        }
    }

}
