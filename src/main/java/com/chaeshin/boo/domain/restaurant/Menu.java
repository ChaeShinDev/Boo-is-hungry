package com.chaeshin.boo.domain.restaurant;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Menu {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_id")
    private Long id;

    private String name;
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @Builder
    public Menu(String name, String imageUrl, Restaurant restaurant) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.restaurant = restaurant;

        // 양방향 연관관계 맺어주기
        restaurant.getMenus().add(this);
    }

}
