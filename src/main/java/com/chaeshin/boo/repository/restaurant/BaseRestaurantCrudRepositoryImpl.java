package com.chaeshin.boo.repository.restaurant;

import com.chaeshin.boo.domain.restaurant.Restaurant;
import com.chaeshin.boo.domain.review.Review;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BaseRestaurantCrudRepositoryImpl implements BaseRestaurantCrudRepository {

    @PersistenceContext EntityManager em;

    /**
     * Review의 ID로 해당하는 식당 조회
     * @param reviewId
     * @return
     */
    @Override
    public Restaurant findByReviewId(Long reviewId) {
        return em.createQuery("select r from Review r where r.id = :reviewId", Review.class)
                .setParameter("reviewId", reviewId)
                .getSingleResult()
                .getRestaurant();
    }

    /**
     * Restaurant의 ID로 메뉴를 fetch join하여 조회
     * @param restaurantId
     * @return
     */
    @Override
    public Restaurant findByIdWithMenus(Long restaurantId) {
        return em.createQuery("select rt from Restaurant rt" +
                " left join fetch rt.menus" +
                " where rt.id = :restaurantId",Restaurant.class)
                .setParameter("restaurantId", restaurantId)
                .getSingleResult();
    }

    @Override
    public List<Restaurant> findAllByNameContaining(String name) {
        return em.createQuery("select rt from Restaurant rt" +
                " where rt.name like :name", Restaurant.class)
                .setParameter("name", name).getResultList();
    }


}
