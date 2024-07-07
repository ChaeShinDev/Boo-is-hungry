package com.chaeshin.boo.repository.restaurant.menu;

import com.chaeshin.boo.domain.restaurant.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends BaseMenuCrudRepository, JpaRepository<Menu, Long> {
}
