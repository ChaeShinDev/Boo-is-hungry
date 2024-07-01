package com.chaeshin.boo.domain.restaurant;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
public class Menu {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_id")
    private Long id;

    private String name;
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    public Menu() {
    }

    @Builder
    public Menu(String name, String imageUrl, Restaurant restaurant) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.restaurant = restaurant;
    }
}
