package com.example.warningmarket.domain.item.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemImage {

    @Id
    @GeneratedValue
    @Column(name = "item_image_id")
    private Long id;

    @JoinColumn(name = "item_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Item item;

    private String imageUrl;

    public void setItem(Item item) {
        this.item = item;
    }

    public ItemImage(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}
