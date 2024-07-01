package com.chaeshin.boo.repository.user;

import com.chaeshin.boo.domain.User;
import java.util.List;

public interface BaseUserCrudRepository {

    /**
     * 회원 닉네임으로 검색
     * @param nickname
     */
    List<User> findByNickname(String nickname);

    /**
     * 회원 구글 id로 검색
     * @param email
     */
    List<User> findByGoogleId(String googleId);

    /**
     * ID로 회원 조회 후 param으로 주어진 nickname으로 변경
     * @param id
     * @param nickname
     */
    void updateNickname(Long id, String nickname);

}
