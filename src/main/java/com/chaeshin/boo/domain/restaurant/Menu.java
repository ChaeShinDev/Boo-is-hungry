package com.chaeshin.boo.domain.restaurant;

import jakarta.persistence.*;
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
}
