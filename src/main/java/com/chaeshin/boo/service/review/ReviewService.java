package com.chaeshin.boo.service.review;

import com.chaeshin.boo.controller.dto.review.ReviewDto;
import com.chaeshin.boo.domain.Member;
import com.chaeshin.boo.domain.review.Review;
import com.chaeshin.boo.domain.review.TranslatedReview;
import com.chaeshin.boo.exception.NoSuchReviewException;
import com.chaeshin.boo.exception.SameLanguageException;
import com.chaeshin.boo.repository.member.MemberRepository;
import com.chaeshin.boo.repository.review.ReviewRepository;
import com.chaeshin.boo.service.review.translatedReview.TranslatedReviewService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final TranslatedReviewService translatedReviewService; // 리뷰 번역.


    /**
     * 리뷰 생성 및 등록.
     * @param review  Controller 로부터 전달 받은 저장할 Review 엔티티.
     * @return reviewId
     */
    @Transactional
    public Long createReview(Review review) {

        Review savedReview = reviewRepository.save(review); // 생성된 리뷰 저장. PK 생성 및 할당을 위해 시행.
        Member reviewWriter = savedReview.getMember();

        try{
            // 해당 Review의 언어 감지를 위해 작성자의 선호 언어로 한 번 번역함.
            // 이때, JPA 영속성 컨텍스트에 저장되어 있는 savedReview를 인자로 보낸다.
            // 번역을 생성하는 과정에서 해당 Review Entity의 본문 언어코드를 설정해줄 것이기 때문이다.
            // 이는 JPA의 변경 감지를 활용한 방식.
            translatedReviewService.createTranslatedReview(savedReview, reviewWriter.getLangCode());
        }
        // 리뷰 생성 시 리뷰 본문의 언어를 초기화하기 위해 리뷰 번역을 하는 것이므로,
        // TranslationService 에서 발생하는 SameLanguageException 을 catch 하여 아무 것도 하지 않는 방식으로 Suppress.
        catch (SameLanguageException e){}
        return savedReview.getId();
    }

    /**
     * Review 단 건 조회.
     * @return
     */
    public Review getReviewById(Long reviewId) throws NoSuchReviewException {
        Optional<Review> found = reviewRepository.findById(reviewId);

        if(found.isPresent()){
            return found.get();
        }
        else {
            throw new NoSuchReviewException("해당 리뷰가 존재하지 않습니다.");
        }
    }

    /**
     * Member ID로 이미지와 함께 Review 조회.
     * @param memberId
     * @return
     */
    public List<Review> getAllByMemberIdWithImage(Long memberId){
        return reviewRepository.findAllByMemberIdWithImage(memberId);
    }

    /**
     * Member ID로 이미지와 함께 Review 조회.
     * @param restaurantId
     * @return
     */
    public List<Review> getAllByRestaurantIdWithImage(Long restaurantId){
        return reviewRepository.findAllByRestaurantIdWithImage(restaurantId);
    }

    /**
     * 리뷰 업데이트. 추후 제대로 업데이트 되었는지 확인이 필요한 경우를 상정해 해당 review의 ID(PK) 반환하도록 설계.
     * @param reviewId
     * @param reviewDto
     * @return
     */
    @Transactional
    public void updateReview(Long reviewId, ReviewDto reviewDto) throws NoSuchReviewException {

        Optional<Review> found = reviewRepository.findById(reviewId);
        if (found.isPresent()) {
            Review foundReview = found.get();

            foundReview.updateTitle(reviewDto.getTitle());
            foundReview.updateBody(reviewDto.getBody());
            foundReview.updateScore(reviewDto.getScore());
        }
        else {
            throw new NoSuchReviewException("해당 리뷰가 존재하지 않습니다.");
        }
    }

    /**
     * 리뷰 삭제. 해당 리뷰가 존재하지 않을 경우 Exception 없이 조용히 종료된다.
     * @param reviewId
     */
    @Transactional
    public void deleteReview(Long reviewId){
        reviewRepository.deleteById(reviewId);
    }

    /**
     * 리뷰 전체 삭제.
     */
    @Transactional
    public void deleteAll(){
        reviewRepository.deleteAll();
    }

}
