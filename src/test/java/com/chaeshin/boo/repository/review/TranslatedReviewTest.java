package com.chaeshin.boo.repository.review;

import com.chaeshin.boo.domain.Member;
import com.chaeshin.boo.domain.restaurant.Restaurant;
import com.chaeshin.boo.domain.review.Review;
import com.chaeshin.boo.domain.review.TranslatedReview;
import com.chaeshin.boo.repository.member.MemberRepository;
import com.chaeshin.boo.repository.restaurant.RestaurantRepository;
import com.chaeshin.boo.repository.review.translatedReview.TranslatedReviewRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class TranslatedReviewTest {

    @Autowired
    TranslatedReviewRepository translatedReviewRepository;
    @Autowired ReviewRepository reviewRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired RestaurantRepository restaurantRepository;


    @Test
    void 리뷰_번역_생성(){
        // given
        Member member = new Member();
        Restaurant res = new Restaurant();

        Member savedMember = memberRepository.save(member);
        Restaurant savedRes = restaurantRepository.save(res);

        Review review = Review.builder().member(savedMember).restaurant(savedRes).build();

        memberRepository.save(member);
        Review savedReview = reviewRepository.save(review);

        TranslatedReview transR = TranslatedReview.builder().review(savedReview).build();

        // when
        TranslatedReview savedTr = translatedReviewRepository.save(transR);

        // then
        Assertions.assertNotNull(savedTr);
    }

    @Test
    void 리뷰_번역_조회(){
        // given
        Member member = new Member();
        Restaurant res = new Restaurant();

        Member savedMember = memberRepository.save(member);
        Restaurant savedRes = restaurantRepository.save(res);

        Review review = Review.builder().member(savedMember).restaurant(savedRes).build();
        Review savedReview = reviewRepository.save(review);

        TranslatedReview transTr = TranslatedReview.builder().review(savedReview).build();

        // when
        TranslatedReview savedTr = translatedReviewRepository.save(transTr);
        Optional<TranslatedReview> found = translatedReviewRepository.findById(savedTr.getId());

        // then
        Assertions.assertTrue(found.isPresent());
        Assertions.assertEquals(found.get().getId(), savedTr.getId());
    }

    @Test
    void 리뷰_번역_수정(){
        // given
        Member member = new Member();
        Restaurant res = new Restaurant();

        Member savedMember = memberRepository.save(member);
        Restaurant savedRes = restaurantRepository.save(res);

        Review review = Review.builder().member(savedMember).restaurant(savedRes).build();
        Review savedReview = reviewRepository.save(review);

        TranslatedReview transTr = TranslatedReview.builder().review(savedReview).build();

        // when
        TranslatedReview savedTr = translatedReviewRepository.save(transTr);
        translatedReviewRepository.updateTranslatedReview(savedTr.getId(), "changed!");

        Optional<TranslatedReview> found = translatedReviewRepository.findById(savedTr.getId());

        // then
        Assertions.assertTrue(found.isPresent());
        Assertions.assertEquals(found.get().getBody(), "changed!");


    }

    @Test
    void 리뷰_번역_삭제(){
        // given
        Member member = new Member();
        Restaurant res = new Restaurant();

        Member savedMember = memberRepository.save(member);
        Restaurant savedRes = restaurantRepository.save(res);

        Review review = Review.builder().member(savedMember).restaurant(savedRes).build();
        Review savedReview = reviewRepository.save(review);

        TranslatedReview transTr = TranslatedReview.builder().review(savedReview).build();
        TranslatedReview savedTr = translatedReviewRepository.save(transTr);

        // when
        translatedReviewRepository.delete(savedTr);
        Optional<TranslatedReview> found = translatedReviewRepository.findById(savedTr.getId());

        // then
        Assertions.assertFalse(found.isPresent());
    }

    @Test
    void 리뷰_id_전체조회(){

        // given
        Member member = new Member();
        Restaurant res = new Restaurant();

        Member savedMember = memberRepository.save(member);
        Restaurant savedRes = restaurantRepository.save(res);

        Review review = Review.builder().member(savedMember).restaurant(savedRes).build();
        Review savedReview = reviewRepository.save(review);

        TranslatedReview tr1 = TranslatedReview.builder().review(savedReview).build();
        TranslatedReview tr2 = TranslatedReview.builder().review(savedReview).build();

        TranslatedReview tr1Saved = translatedReviewRepository.save(tr1);
        TranslatedReview tr2Saved = translatedReviewRepository.save(tr2);

        // when
        List<TranslatedReview> trs = translatedReviewRepository.findAllByReviewId(savedReview.getId());

        // then
        Assertions.assertEquals(2, trs.size());

    }


}
