package com.chaeshin.boo.repository.review;

import com.chaeshin.boo.domain.User;
import com.chaeshin.boo.domain.restaurant.Restaurant;
import com.chaeshin.boo.domain.review.Review;
import com.chaeshin.boo.repository.restaurant.RestaurantRepository;
import com.chaeshin.boo.repository.user.UserRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class ReviewRepositoryTest {

    @Autowired ReviewRepository reviewRepository;
    @Autowired UserRepository userRepository;
    @Autowired RestaurantRepository restaurantRepository;

    @Test
    void 리뷰_저장(){
        // given
        Review review = new Review();
        ReflectionTestUtils.setField(review, "id", 1L);

        // when
        Review found = reviewRepository.save(review);

        // then
        Assertions.assertNotNull(found);
        Assertions.assertEquals(found.getId(), review.getId());
    }

    @Test
    void 리뷰_ID_조회(){

        // given
        Review review = new Review();
        ReflectionTestUtils.setField(review, "id", 1L);
        reviewRepository.save(review);

        // when
        Optional<Review> found = reviewRepository.findById(review.getId());

        // then
        Assertions.assertTrue(found.isPresent());
        Assertions.assertEquals(found.get().getId(), review.getId());
    }

    @Test
    void 회원_ID_리뷰_조회(){
        // given
        User user = new User();
        ReflectionTestUtils.setField(user, "nickname", "testUser");
        ReflectionTestUtils.setField(user, "id", 1L);
        userRepository.save(user);

        // when
        Review review = new Review();
        ReflectionTestUtils.setField(review, "user", user);
        reviewRepository.save(review);

        List<Review> found = reviewRepository.findAllByUserId(user.getId());

        // then
        Assertions.assertFalse(found.isEmpty());
        Assertions.assertEquals(found.get(0).getUser().getId(), user.getId());

    }

    @Test
    void 식당_ID_리뷰_조회(){
        // given
        Restaurant res = new Restaurant();
        ReflectionTestUtils.setField(res, "name", "testRes");
        ReflectionTestUtils.setField(res, "id", 1L);
        restaurantRepository.save(res);

        // when
        Review review = new Review();
        ReflectionTestUtils.setField(review, "restaurant", res);
        reviewRepository.save(review);

        List<Review> found = reviewRepository.findAllByRestaurantId(res.getId());

        // then
        Assertions.assertFalse(found.isEmpty());
        Assertions.assertEquals(found.get(0).getRestaurant().getId(), res.getId());
    }


    /**
     * Review 수정의 경우 Entity에 편의 기능 - 제목 변경 / 본문 변경 - 을 만들어 두었다.
     * <p></p>
     * 이를 활용하여 Service Layer 에서 제공하는 것이 적절하지 않을까? 하는 생각이 든다!
     */
    @Test
    void 리뷰_수정(){
    }

    @Test
    void 리뷰_삭제(){
        // given
        Review review = new Review();
        ReflectionTestUtils.setField(review, "id", 1L);
        reviewRepository.save(review);

        // when
        reviewRepository.delete(review);
        Optional<Review> found = reviewRepository.findById(review.getId());

        // then
        Assertions.assertFalse(found.isPresent());
    }
}
