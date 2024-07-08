package com.chaeshin.boo.repository.restaurant;

import com.chaeshin.boo.domain.restaurant.Restaurant;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class BaseRestaurantCrudRepositoryImpl implements BaseRestaurantCrudRepository {

    @PersistenceContext EntityManager em;

    @Override
    public Restaurant findByIdWithMenu(Long restaurantId) {
        return em.createQuery("select r from Restaurant r left join fetch r.menus where r.id = :restaurantId", Restaurant.class)
                .setParameter("restaurantId", restaurantId)
                .getSingleResult();
    }
}
