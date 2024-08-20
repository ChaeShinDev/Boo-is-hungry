package com.chaeshin.boo.utils.jwt;

public interface TokenBlacklist {

  /*블랙리스트에 토큰을 저장(Key: 토큰, Value: 파기시각)*/
  void put(String token, String date);

  /*블랙리스트에 토큰이 저장되어 있는지 조회*/
  boolean containsKey(String token);
}
