package com.chaeshin.boo.repository.restaurant;

import com.chaeshin.boo.domain.restaurant.Restaurant;
import com.chaeshin.boo.repository.user.BaseUserCrudRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class BaseRestaurantCrudRepositoryImpl implements BaseRestaurantCrudRepository {

    @PersistenceContext EntityManager em;
    @Override
    public List<Restaurant> findByName(String name) {

        // Case A : 공백이 name 으로 주어진 경우 - 빈 List 반환
        if(name == null || name.trim().isEmpty()){
            return Collections.emptyList();
        }

        // Case B : 해당 이름으로부터 공백 기준 분리한 단어 혹은 단어 전체와 name이 일치하는 Restaurant 을 List로 반환.
        List<String> parsed = Arrays.stream(name.split(" ")).toList(); // Stream API 가 for 보다 최적화가 덜 되어 느리다는 분석을 본 적이 있다.
        return em.createQuery("select r from Restaurant r where r.name in :names or r.name = :name", Restaurant.class)
                .setParameter("names", parsed)
                .setParameter("name", name)
                .getResultList();
    }
}
