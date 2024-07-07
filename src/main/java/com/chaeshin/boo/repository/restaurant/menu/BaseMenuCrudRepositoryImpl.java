package com.chaeshin.boo.repository.restaurant.menu;

import com.chaeshin.boo.domain.restaurant.Menu;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class BaseMenuCrudRepositoryImpl implements BaseMenuCrudRepository {

    @PersistenceContext EntityManager em;

    /**
     * 주어진 식당의 메뉴 전체 반환
     * @param restaurantId
     * @return
     */
    public List<Menu> findAllByRestaurant(Long restaurantId){
        return em.createQuery("select m from Menu m where m.restaurant.id = :restaurantId", Menu.class)
                .setParameter("restaurantId", restaurantId)
                .getResultList();
    }
}
