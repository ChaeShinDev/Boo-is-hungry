package com.chaeshin.boo.service.restaurant;

import com.chaeshin.boo.domain.restaurant.Restaurant;
import com.chaeshin.boo.repository.restaurant.RestaurantRepository;
import com.chaeshin.boo.service.KakaoGeoService;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.NoResultException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final KakaoGeoService kakaoGeoService;

    /**
     * RestaurantService 의 초기화 콜백.
     * <br></br>
     * <br></br>
     * <b><i>한국외국어대학 서울캠퍼스 반경 788m 이내의 음식점, 카페 정보를 가져와 Restaurant 엔티티로 만든 후 영속성 컨텍스트에 저장.</i></b>
     * <br></br>
     * <br></br>
     * 사용자에게 제공하기 위한 식당의 Pool 을 조성하는 작업.
     */
    @PostConstruct
    public void init(){
        kakaoGeoService.initAllRestaurant();
    }

    /**
     * 식당 저장.
     * @param restaurant
     * @return Restaurant saved.
     * @throws IllegalArgumentException - 저장할 엔티티가 비어있을 때.
     * @throws org.springframework.dao.OptimisticLockingFailureException - @Version 을 통해 낙관적 락 전략을 설정하였으나 실패한 경우.
     */
    @Transactional
    public Restaurant createRestaurant(Restaurant restaurant){
        return restaurantRepository.save(restaurant);
    }

    /**
     * 식당 단 건 조회.
     * @param restaurantId
     * @return
     * @throws NoSuchElementException - 해당하는 레스토랑이 존재하지 않을 때 발생.
     */
    public Restaurant getRestaurantByRestaurantId(Long restaurantId) throws NoSuchElementException {
        Optional<Restaurant> foundRestaurant = restaurantRepository.findById(restaurantId);

        if(foundRestaurant.isPresent()){
            return foundRestaurant.get();
        }
        else {
            throw new NoSuchElementException("해당 레스토랑이 존재하지 않습니다.");
        }
    }

    /**
     * 리뷰 ID로 식당 조회.
     * @param reviewId
     * @return
     * @throws NoSuchElementException  해당하는 레스토랑이 존재하지 않을 때 발생.
     */
    public Restaurant getRestaurantByReviewId(Long reviewId) throws NoSuchElementException {
        try{
            return restaurantRepository.findByReviewId(reviewId);
        }
        catch (NoResultException e){
            // Hibernate가 제공하는 Interface 단에서 발생하는 예외는 NoResultException이나, 일관성과 편의성을 위해 NoSuchElementException 처리.
            throw new NoSuchElementException("해당 레스토랑이 존재하지 않습니다.");
        }
    }

    /**
     * 식당 전체 목록 조회.
     * @return 식당 전체 목록.
     */
    public List<Restaurant> getAll(){
        return restaurantRepository.findAll();
    }


    /**
     * 레스토랑 삭제.
     * @param restaurantId
     */
    @Transactional
    public void deleteRestaurant(Long restaurantId){
        restaurantRepository.deleteById(restaurantId);
    }

    /**
     * 레스토랑 전체 삭제.
     */
    @Transactional
    public void deleteAll(){
        restaurantRepository.deleteAll();
    }

}
