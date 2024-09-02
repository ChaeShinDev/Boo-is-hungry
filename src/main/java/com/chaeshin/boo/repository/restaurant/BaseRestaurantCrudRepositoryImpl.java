package com.chaeshin.boo.repository.restaurant;

import com.chaeshin.boo.domain.restaurant.Restaurant;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import org.springframework.stereotype.Repository;

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
        return em.createQuery("select r.restaurant from Review r"
                + " where r.id = :reviewId", Restaurant.class)
                .setParameter("reviewId", reviewId)
                .getSingleResult();
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
