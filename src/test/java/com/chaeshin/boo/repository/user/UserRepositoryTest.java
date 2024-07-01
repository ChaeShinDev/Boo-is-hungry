package com.chaeshin.boo.repository.user;

import com.chaeshin.boo.domain.User;
import com.chaeshin.boo.domain.review.Review;
import com.chaeshin.boo.repository.user.UserRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

// import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
// import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
// import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

// Q. 아래의 Annotation 조합으로도 테스트가 가능함. 어떤 차이일까...?
// @DataJpaTest
// @AutoConfigureTestDatabase(replace = Replace.NONE)
@SpringBootTest
@Transactional
public class UserRepositoryTest {
    @Autowired UserRepository userRepository;

    @Test
    void 회원_가입_ID_조회(){
        User user = new User();
        ReflectionTestUtils.setField(user, "id", 1L);
        ReflectionTestUtils.setField(user, "nickname", "test");
        userRepository.save(user);

        Optional<User> found = userRepository.findById(1L); // Optional<T> : 해당 객체가 null 일지 아닐지 확실하지 않은 경우에 안전하게 객체를 확인하고 처리하기 위한 일종의 Wrapper Class.

        Assertions.assertTrue(found.isPresent()); // True : 영속성 컨텍스트에 해당 Entity 가 존재함.
        Assertions.assertEquals(user.getNickname(), found.get().getNickname());
    }

    @Test
    void 닉네임_회원_조회(){
        User user = new User();
        ReflectionTestUtils.setField(user, "nickname", "test");
        ReflectionTestUtils.setField(user, "id", 1L);

        userRepository.save(user);

        List<User> found = userRepository.findByNickname("test");

        for(User usr : found){
            System.out.println("usr.nickname = " + usr.getNickname());
        }

        Assertions.assertFalse(found.isEmpty());
    }

    @Test
    void 구글_ID_회원_조회(){
        User user = new User();
        ReflectionTestUtils.setField(user, "googleId", "blah@google.com");
        ReflectionTestUtils.setField(user, "id", 1L);

        userRepository.save(user);

        List<User> found = userRepository.findByGoogleId("blah@google.com");

        for(User usr : found){
            System.out.println("usr.googleId = " + usr.getGoogleId());
        }

        Assertions.assertFalse(found.isEmpty());
    }

    @Test
    void 회원_닉네임_변경(){
        User user = new User();
        ReflectionTestUtils.setField(user, "id", 1L);
        ReflectionTestUtils.setField(user, "nickname", "before");

        userRepository.save(user);

        userRepository.updateNickname(user.getId(), "after");

        Optional<User> found = userRepository.findById(user.getId());

        Assertions.assertTrue(found.isPresent());
        Assertions.assertEquals(found.get().getNickname(), "after");
    }

    @Test
    void 회원_삭제(){
        // given
        User user = new User();
        ReflectionTestUtils.setField(user, "id", 1L);
        userRepository.save(user);

        // when
        userRepository.delete(user);
        Optional<User> found = userRepository.findById(user.getId());

        // then
        Assertions.assertFalse(found.isPresent());
    }
}
