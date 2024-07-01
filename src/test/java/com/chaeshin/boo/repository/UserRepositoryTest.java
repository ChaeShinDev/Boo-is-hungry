package com.chaeshin.boo.repository;

import com.chaeshin.boo.domain.User;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;


    @Test
    public void 회원가입() {
        /*given*/
        User user = createUser("1", "kim", "ko");

        /*when*/
        User savedUser = userRepository.save(user);

        /*then*/
        Assertions.assertEquals(user, savedUser);
    }

    @Test
    public void 구글ID로_조회() {
        /*given*/
        User user = createUser("1", "kim", "ko");
        User savedUser = userRepository.save(user);

        /*when*/
        User searchedUser = userRepository.findByGoogleId("1");

        /*then*/
        Assertions.assertEquals(savedUser, searchedUser);
    }

    private User createUser(String googleId, String nickname, String userLang) {
        return new User().builder()
                .googleId(googleId).nickname(nickname).userLang(userLang).build();
    }
}
