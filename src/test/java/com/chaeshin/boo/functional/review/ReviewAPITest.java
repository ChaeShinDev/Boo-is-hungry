package com.chaeshin.boo.functional.review;

import com.chaeshin.boo.controller.dto.review.ReviewDto;
import com.chaeshin.boo.domain.Member;
import com.chaeshin.boo.domain.restaurant.Category;
import com.chaeshin.boo.domain.restaurant.Restaurant;
import com.chaeshin.boo.domain.review.Review;
import com.chaeshin.boo.service.member.MemberService;
import com.chaeshin.boo.service.member.auth.JwtAuthService;
import com.chaeshin.boo.service.restaurant.RestaurantService;
import com.chaeshin.boo.service.review.ReviewService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


// @WebMvcTest - Web Layer 와 관련된 Bean에 대해서만 Configuration 하기 때문에, 아래와 같은 통합테스트에서는 적합하지 않다.
@SpringBootTest
@AutoConfigureMockMvc // @SpringBootTest는 MockMvc를 Bean으로 생성하지 않기 때문에, @AutoConfigureMockMvc를 사용하여 MockMvc를 자동으로 설정해준다.
@Transactional
public class ReviewAPITest {

    final String AUTH = "Bearer token"; // POST 요청 시 헤더에 담길 Authorization 값.
    final String ACCESSTOKEN = "token"; // Authorization 값으로부터 Parse 된 JWT 토큰 값.

    @Autowired // @Autowired가 있어도, @AutoConfigureMockMvc가 없으면 해당 Bean 자체를 찾지 못해 주입이 불가하다.
    MockMvc mockMvc;

    @Autowired
    ReviewService reviewService;

    @MockBean
    JwtAuthService jwtAuthService;

    @Autowired
    RestaurantService restaurantService;

    @Autowired
    MemberService memberService;

    @Autowired ObjectMapper objectMapper;

    Member member;
    Restaurant restaurant;

    @BeforeEach
    void setUp() {
        // 테스트를 위한 멤버, 레스토랑 객체 생성.
        Long memberId = memberService.join("johndoe@gmail.com"); // 테스트를 위한 멤버 생성 및 가입.
        member = memberService.getMemberById(memberId).get(); // 같은 Transaction 내이므로 해당 멤버가 존재하지 않을 가능성 0.

        restaurant = Restaurant.builder()
                .name("제발되라식당")
                .address("경남 사천시")
                .phone("1577")
                .category(Category.KOREAN)
                .build();

        restaurant = restaurantService.createRestaurant(restaurant);
    }

    /**
     * 리뷰 생성 테스트.
     * <br></br>
     * <br></br>
     * <b><주의사항> : @Transactional 없을 경우 LazyInitializationException 발생. </b>
     * <br></br>
     * <small><i>org.hibernate.LazyInitializationException: failed to lazily initialize a collection of role: com.chaeshin.boo.domain.restaurant.Restaurant.reviews, could not initialize proxy - no Session</i></small>
     * <br></br>
     * <br></br>
     * <br></br>
     * <b>원인</b>
     * <br></br>
     * <br></br>
     * <i>지연 로딩 전략을 사용하는 엔티티의 특정 필드가 있을 때, 해당 필드에 대한 참조를 시도하였으나 - 지연로딩 전략 상 이때 이 객체는 프록시 객체이다. -  이미 <b>영속성 컨텍스트가 종료되어(DB Session 종료)</b> 해당 필드를 초기화할 수 없는 상황이 발생함.</i>
     * <br></br>
     * <br></br>
     * - 현 테스트 메서드 내에선 복수의 Transaction 실행된다.
     * <br></br>
     * - OSIV(Open Session In View)는 DB 세션 - 커넥션풀로부터 DB와의 커넥션을 받아 유지하고 있는 상태 - 을 HTTP 요청 시작 ~ 끝(View rendering phase)까지 유지하는 것을 말한다.
     * <br></br>
     * - `spring.jap.open-in-view` 는 default = true. 현재의 프로젝트는 별도 설정 없으므로 true.
     * <br></br>
     * - 따라서 andDo() 부분이 실행되는 시점엔 이미 HTTP Request에 대한 응답이 생성되어 보내졌으므로 DB 세션이 종료된 시점이므로 해당 문제 발생.
     * <br></br>
     * - 하지만 해당 테스트 메서드에 @Transactional 선언하면 해당 테스트 메서드 처리 완료 시점까지가 DB 세션의 Lifecycle 이 되기에 가능한 것.
     * <br></br>
     * <br></br>
     * <br></br>
     * <b>주의점</b>
     * <br></br>
     * <br></br>
     * 1. OSIV 는 DB Connection을 오래 유지하기 때문에 실시간 트래픽이 많이 발생하는 경우 커넥션 풀의 빠른 고갈로 이어지는 주요한 원인이 되기도 한다.
     * <br></br>  - 따라서 분별력 있는 활용이 필요함.
     * <br></br>
     *
     * @throws Exception
     */
    @Test
    @DisplayName("POST : review/create/{restuarnt_id}/")
    void 리뷰_생성() throws Exception {

        // Given
        ReviewDto reviewDto = ReviewDto.builder()
                .title("Test Title")
                .body("Test Body")
                .score(5)
                .build();

        // When
        when(jwtAuthService.parseToken(AUTH)).thenReturn(ACCESSTOKEN);
        when(jwtAuthService.verifyAccessToken(ACCESSTOKEN)).thenReturn(true);
        when(jwtAuthService.getMemberIdFromToken(ACCESSTOKEN)).thenReturn(member.getId());

        // Then
        mockMvc.perform(post("/review/create/" + restaurant.getId().toString() + "/")
                        .header("Authorization", AUTH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewDto)))
                .andDo(result -> { // 양방향 연관관계 정상 설정 완료 여부 확인 위한 로직.
                    Restaurant relatedRes = restaurantService.getRestaurantByRestaurantId(restaurant.getId());
                    Assertions.assertEquals(1, relatedRes.getReviews().size());
                })
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("GET : review/detail/{review_id}/")
    void 리뷰_상세() throws Exception {
        // Given
        Review review = Review.builder()
                .restaurant(restaurant)
                .member(member)
                .title("title")
                .body("body")
                .score(4)
                .build();

        Long reviewId = reviewService.createReview(review);

        // When
        when(jwtAuthService.parseToken(AUTH)).thenReturn(ACCESSTOKEN);
        when(jwtAuthService.verifyAccessToken(ACCESSTOKEN)).thenReturn(true);
        when(jwtAuthService.getMemberIdFromToken(ACCESSTOKEN)).thenReturn(member.getId());


        // then
        mockMvc.perform(get("/review/detail/" + reviewId.toString() + "/")
                .header("Authorization", AUTH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("PUT : /review/update/{review_id}/")
    void 리뷰_수정() throws Exception {
        // Given
        Review review = Review.builder()
                .restaurant(restaurant)
                .member(member)
                .title("title")
                .body("body")
                .score(4)
                .build();

        Long reviewId = reviewService.createReview(review);

        // When
        when(jwtAuthService.parseToken(AUTH)).thenReturn(ACCESSTOKEN);
        when(jwtAuthService.verifyAccessToken(ACCESSTOKEN)).thenReturn(true);
        when(jwtAuthService.getMemberIdFromToken(ACCESSTOKEN)).thenReturn(member.getId());

        // then
        mockMvc.perform(put("/review/update/" + reviewId.toString() + "/")
                .header("Authorization", AUTH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ReviewDto.builder().title("new title").body("new body").score(5).build()))
        )
                .andDo(result -> {
                    Review updatedReview = reviewService.getReviewById(reviewId);
                    Assertions.assertEquals("new title", updatedReview.getTitle());
                    Assertions.assertEquals("new body", updatedReview.getBody());
                    Assertions.assertEquals(5, updatedReview.getScore());
                })
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE : /review/update/{review_id}/")
    void 리뷰_삭제() throws Exception {
        // Given
        Review review = Review.builder()
                .restaurant(restaurant)
                .member(member)
                .title("title")
                .body("body")
                .score(4)
                .build();

        Long reviewId = reviewService.createReview(review);

        // When
        when(jwtAuthService.parseToken(AUTH)).thenReturn(ACCESSTOKEN);
        when(jwtAuthService.verifyAccessToken(ACCESSTOKEN)).thenReturn(true);
        when(jwtAuthService.getMemberIdFromToken(ACCESSTOKEN)).thenReturn(member.getId());

        // then
        mockMvc.perform(delete("/review/update/" + reviewId.toString() + "/")
                .header("Authorization", AUTH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET : review/user/{user_id}/")
    void 회원PK로_리뷰_조회() throws Exception {
        // given
        Review review = Review.builder()
                .restaurant(restaurant)
                .member(member)
                .title("title")
                .body("body")
                .score(4)
                .build();

        Long reviewId = reviewService.createReview(review);

        // then
        mockMvc.perform(get("/review/user/" + member.getId().toString() + "/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET : review/restaurant/simpleinfo/{review_id}/")
    void 리뷰PK_식당_간단정보_조회() throws Exception {
        // given
        Review review = Review.builder()
                .restaurant(restaurant)
                .member(member)
                .title("title")
                .body("body")
                .score(4)
                .build();

        Long reviewId = reviewService.createReview(review);

        // when
        when(jwtAuthService.parseToken(AUTH)).thenReturn(ACCESSTOKEN);
        when(jwtAuthService.verifyAccessToken(ACCESSTOKEN)).thenReturn(true);
        when(jwtAuthService.getMemberIdFromToken(ACCESSTOKEN)).thenReturn(member.getId());

        // then
        mockMvc.perform(get("/review/restaurant/simpleinfo/" + review.getId().toString() + "/")
                .header("Authorization", AUTH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET : review/restaurant/{restaurant_id}/")
    void 식당_리뷰_전체조회() throws Exception {
        // given
        Review review = Review.builder()
                .restaurant(restaurant)
                .member(member)
                .title("title")
                .body("body")
                .score(4)
                .build();

        Long reviewId = reviewService.createReview(review);

        // then
        mockMvc.perform(get("/review/restaurant/" + restaurant.getId().toString() + "/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET : review/myreview/")
    void 나의_리뷰_조회() throws Exception {
        // given
        Review review = Review.builder()
                .restaurant(restaurant)
                .member(member)
                .title("title")
                .body("body")
                .score(4)
                .build();

        Long reviewId = reviewService.createReview(review);

        // when
        when(jwtAuthService.parseToken(AUTH)).thenReturn(ACCESSTOKEN);
        when(jwtAuthService.verifyAccessToken(ACCESSTOKEN)).thenReturn(true);
        when(jwtAuthService.getMemberIdFromToken(ACCESSTOKEN)).thenReturn(member.getId());

        // then
        mockMvc.perform(get("/review/myreview/")
                .header("Authorization", AUTH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
