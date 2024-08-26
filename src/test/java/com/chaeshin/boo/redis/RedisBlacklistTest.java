package com.chaeshin.boo.redis;

import com.chaeshin.boo.utils.jwt.TokenBlacklist;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class RedisBlacklistTest {

    @Autowired
    TokenBlacklist redisBlacklist;

    @Test
    void 블랙리스트_추가() {

        // given
        String key = "key;";
        String value = new Date(new Date().getTime() + 10000).toString();

        // when
        redisBlacklist.put(key, value);

        // then
        Assertions.assertTrue(redisBlacklist.containsKey(key));
    }

    @Test
    void 블랙리스트_만료_삭제(){
        // given
        String key = "key;";
        String value = new Date(new Date().getTime() + 10000).toString();
        redisBlacklist.put(key, value);

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(); // 스케줄링을 위한 ExecutorService 생성

        // when
        boolean existBeforeExpiration = redisBlacklist.containsKey(key); // 만료 전 토큰 존재 여부.
        boolean existAfterExpiration = true;
        try {
            Thread.sleep(10001); // 토큰 만료 시간이 지나가는 때까지 대기.
            existAfterExpiration = redisBlacklist.containsKey(key); // 만료 후 토큰 존재 여부.
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }

        // then
        Assertions.assertTrue(existBeforeExpiration);
        Assertions.assertFalse(existAfterExpiration);
    }
}
