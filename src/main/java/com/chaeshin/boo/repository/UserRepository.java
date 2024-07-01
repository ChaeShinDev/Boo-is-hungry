package com.chaeshin.boo.repository;

import com.chaeshin.boo.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    /*구글ID를 통해 유저 조회*/
    User findByGoogleId(String googleId);
}
