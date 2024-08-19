package com.chaeshin.boo.utils.jwt;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Component;

@Component("hashMapBlacklist")
public class HashMapBlacklist implements TokenBlacklist{

  private ConcurrentHashMap<String, Date> blackList;
  private ScheduledExecutorService scheduler;
  private SimpleDateFormat formatter = new SimpleDateFormat(
      "EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);


  @Override
  public void put(String token, String date) {
    for (int i = 0; i < 100; i++) {
      System.out.println("date = " + date);
    }

    try {
      blackList.put(token, formatter.parse(date));
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
  }


  @Override
  public boolean containsKey(String token) {
    return blackList.containsKey(token);
  }


  /*어플리케이션이 실행될 때마다 블랙리스트 및 삭제 스케쥴러 재시작*/
  @PostConstruct
  private void init() {
    blackList = new ConcurrentHashMap<>();
    scheduler = Executors.newSingleThreadScheduledExecutor();
    scheduler.scheduleAtFixedRate(this::removeExpiredTokens, 0, 1, TimeUnit.HOURS);
  }

  /*블랙리스트 내 토큰 자동 삭제*/
  private void removeExpiredTokens() {
    Date now = new Date();
    blackList.entrySet().removeIf(entry -> entry.getValue().before(now));
  }

  /*어플리케이션이 종료되면 블랙리스트 자동 삭제 스케쥴러 종료*/
  @PreDestroy
  private void cleanup() {
    if (scheduler != null) {
      scheduler.shutdown();
    }
  }
}
