package com.chaeshin.boo.repository.restaurant;

import com.chaeshin.boo.domain.restaurant.Menu;
import com.chaeshin.boo.domain.restaurant.Restaurant;
import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Menu 엔티티의 경우 단독으로 Menu 를 대상으로 쿼리하는 기능 명세가 존재 하지 않는다.
 * <br></br>
 * Menu 를 DB 혹은 영속성 컨텍스트로부터 조회하는 시나리오는 '어떤 레스토랑의 메뉴를 전체 조회한다'만 존재하기 때문에,
 * <br></br>
 * Spring Data JPA 를 상속받는 인터페이스가 아닌 바로 구현체로 구현하도록 하였다.
 * <br></br>
 * <small>Ho Yeon Chae</small>
 */
@Repository
@RequiredArgsConstructor
public class MenuRepository {

    private final EntityManager em;

    /**
     * 주어진 식당의 메뉴 전체 반환
     * @param restaurant
     * @return
     */
    List<Menu> findAllByRestaurant(Long restaurantId){
        return em.createQuery("select m from Menu m where m.restaurant.id = :restaurantId", Menu.class)
                .setParameter("restaurantId", restaurantId)
                .getResultList();
    }

    /**
     * 메뉴 저장
     * @param menu
     */
    void save(Menu menu){em.persist(menu);}
}
