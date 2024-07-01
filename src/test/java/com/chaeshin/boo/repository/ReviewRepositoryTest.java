package com.chaeshin.boo.repository;

import com.chaeshin.boo.domain.User;
import com.chaeshin.boo.domain.restaurant.Category;
import com.chaeshin.boo.domain.restaurant.Menu;
import com.chaeshin.boo.domain.restaurant.Restaurant;
import com.chaeshin.boo.domain.review.Review;
import com.chaeshin.boo.domain.review.ReviewImage;
import com.chaeshin.boo.repository.restaurant.RestaurantRepository;
import com.chaeshin.boo.repository.review.ReviewImageRepository;
import com.chaeshin.boo.repository.review.ReviewRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ReviewImageRepository imageRepository;

    @Test
    public void 리뷰_작성() throws Exception {
        /*given*/
        Restaurant restaurant = restaurantRepository.save(createRestaurant());
        User user = userRepository.save(createUser("kim"));

        /*when*/
        Review review = createReview(user, restaurant);
        Review savedReview = reviewRepository.save(review);

        /*then*/
        assertEquals(review, savedReview);
    }


    @Test
    public void 유저_리뷰_리스트_조회() throws Exception {
        /*given*/
        Restaurant restaurant = restaurantRepository.save(createRestaurant());
        User user = userRepository.save(createUser("kim"));

        /*when*/
        for (int i = 0; i < 20; i++) {
            Review review = reviewRepository.save(createReview(user, restaurant));
            user.getReviews().add(review);
        }

        /*then*/
        // 유저의 리스트에서 확인
        assertEquals(20, user.getReviews().size());

        // 레포지토리에서 확인
        assertEquals(20, reviewRepository.findAllByUserIdWithReviewImages(user.getId()).size());
    }

    @Test
    public void 식당_리뷰_리스트_조회() throws Exception {
        /*given*/
        Restaurant restaurant = restaurantRepository.save(createRestaurant());
        User user = userRepository.save(createUser("kim"));

        /*when*/
        for (int i = 0; i < 20; i++) {
            Review review = reviewRepository.save(createReview(user, restaurant));
            restaurant.getReviews().add(review);
        }

        /*then*/
        // 식당의 리스트에서 확인
        assertEquals(20, restaurant.getReviews().size());

        // 레포지토리에서 확인
        assertEquals(20, reviewRepository.findAllByRestaurantIdWithReviewImages(restaurant.getId()).size());
    }

    @Test
    public void 리뷰_이미지_업로드() throws Exception {
        /*given*/
        Restaurant restaurant = restaurantRepository.save(createRestaurant());
        User user = userRepository.save(createUser("kim"));
        Review review = reviewRepository.save(createReview(user, restaurant));
        user.getReviews().add(review);
        restaurant.getReviews().add(review);

        /*when*/
        ReviewImage image = createReviewImage(review);
        ReviewImage savedImage = imageRepository.save(image);

        /*then*/
        assertEquals(image, savedImage);
    }

    @Test
    public void 리뷰_이미지_조회() throws Exception {
        /*given*/
        Restaurant restaurant = restaurantRepository.save(createRestaurant());
        User user = userRepository.save(createUser("kim"));
        Review review = reviewRepository.save(createReview(user, restaurant));
        user.getReviews().add(review);
        restaurant.getReviews().add(review);

        /*when*/
        for (int i = 0; i < 20; i++) {
            ReviewImage image = imageRepository.save(createReviewImage(review));
            review.getReviewImages().add(image);
        }

        /*then*/
        // 리뷰의 리스트에서 확인
        assertEquals(20, review.getReviewImages().size());

        // 레포지토리에서 확인
        assertEquals(20, reviewRepository.findById(review.getId()).get().getReviewImages().size());
    }


    private Restaurant createRestaurant() {
        return new Restaurant().builder().name("맥도날드").latitude("위도").longitude("경도")
                .category(Category.KOREAN).build();
    }

    private Menu createMenu(Restaurant restaurant) {
        return new Menu().builder().restaurant(restaurant).name("햄버거").build();
    }

    private Review createReview(User user, Restaurant restaurant) {
        return new Review().builder().restaurant(restaurant).user(user).build();
    }

    private ReviewImage createReviewImage(Review review) {
        return new ReviewImage().builder().review(review).build();
    }

    private User createUser(String nickname) {
        return new User().builder().nickname(nickname).build();
    }
}
