package com.chaeshin.boo.repository.restaurant;

import com.chaeshin.boo.domain.Member;
import com.chaeshin.boo.domain.restaurant.Restaurant;
import com.chaeshin.boo.domain.review.Review;
import com.chaeshin.boo.repository.member.MemberRepository;
import com.chaeshin.boo.repository.review.ReviewRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class RestaurantRepositoryTest {

    @Autowired RestaurantRepository restaurantRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired ReviewRepository reviewRepository;

    @Test
    void 식당_저장(){

        // given
        Restaurant res = new Restaurant();
        ReflectionTestUtils.setField(res, "name", "레알라면");

        // when
        Restaurant found = restaurantRepository.save(res);

        // then
        Assertions.assertNotNull(found);
        Assertions.assertEquals(found.getName(), "레알라면");
    }

    @Test
    void 식당_Id_조회(){

        // given
        Restaurant res = new Restaurant();

        // when
        Restaurant created = restaurantRepository.save(res);
        Restaurant found = restaurantRepository.findById(created.getId()).get();

        // then
        Assertions.assertEquals(found.getId(), created.getId());
    }

    @Test
    void 리뷰_id_식당_조회(){

        // given
        Restaurant res = new Restaurant(); // Restaurant 생성
        Member member = new Member(); // Member 생성

        Restaurant savedRes = restaurantRepository.save(res);
        memberRepository.save(member);

        Review review = Review.builder().member(member).restaurant(res).build();

        Review savedReview = reviewRepository.save(review);

        // when
        Restaurant foundRes = restaurantRepository.findByReviewId(savedReview.getId());

        // then
        Assertions.assertEquals(savedRes, foundRes);
    }
}
