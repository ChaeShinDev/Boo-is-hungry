package com.chaeshin.boo.utils.jwt;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("redisBlackList")
@Primary
@RequiredArgsConstructor
@Transactional
public class RedisBlacklist implements TokenBlacklist{
    private final RedisTemplate<String, String> redisTemplate;
    private final SimpleDateFormat formatter = new SimpleDateFormat(
            "EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);

    @Override
    public void put(String token, String date) {
        try {
            redisTemplate.opsForValue().set(token, date);
            Date expireAt = formatter.parse(date);
            redisTemplate.expireAt(token, expireAt);
        }
        catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean containsKey(String token) {
        return redisTemplate.hasKey(token);
    }
}
