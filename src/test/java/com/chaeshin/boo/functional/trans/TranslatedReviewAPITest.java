package com.chaeshin.boo.functional.trans;

import com.chaeshin.boo.domain.Member;
import com.chaeshin.boo.domain.restaurant.Category;
import com.chaeshin.boo.domain.restaurant.Restaurant;
import com.chaeshin.boo.domain.review.Review;
import com.chaeshin.boo.service.member.MemberService;
import com.chaeshin.boo.service.member.auth.JwtAuthService;
import com.chaeshin.boo.service.restaurant.RestaurantService;
import com.chaeshin.boo.service.review.ReviewService;
import com.chaeshin.boo.service.review.translatedReview.TranslatedReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class TranslatedReviewAPITest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    TranslatedReviewService translatedReviewService;

    @Autowired
    ReviewService reviewService;

    @Autowired
    MemberService memberService;

    @MockBean
    JwtAuthService jwtAuthService;

    @Autowired
    RestaurantService restaurantService;

    Member member;

    Restaurant restaurant;

    Review review;

    final String AUTH = "Bearer token";
    final String ACCESSTOKEN = "token";

    @BeforeEach
    void setUp() {
        Long memberId = memberService.join("johndoe@gmail.com");
        restaurant = Restaurant.builder()
                .name("제발되라식당")
                .address("경남 사천시")
                .phone("1577")
                .category(Category.KOREAN)
                .build();

        member = memberService.getMemberById(memberId).get();
        restaurant = restaurantService.createRestaurant(restaurant);

        review = Review.builder()
                .restaurant(restaurant)
                .member(member)
                .title("title")
                .body("body")
                .score(3)
                .build();

        Long reviewId = reviewService.createReview(review);
        review = reviewService.getReviewById(reviewId);
    }

    @Test
    @DisplayName("trans/{review_id}")
    void 리뷰_번역() throws Exception {

        // when
        when(jwtAuthService.parseToken(AUTH)).thenReturn(ACCESSTOKEN);
        when(jwtAuthService.verifyAccessToken(ACCESSTOKEN)).thenReturn(true);
        when(jwtAuthService.getMemberIdFromToken(ACCESSTOKEN)).thenReturn(member.getId());


        // then
        mockMvc.perform(get("/trans/" + review.getId() + "/")
                .header("Authorization", AUTH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }





}
