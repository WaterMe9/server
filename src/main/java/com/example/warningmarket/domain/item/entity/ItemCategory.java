package com.example.warningmarket.domain.item.entity;

import com.example.warningmarket.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemCategory extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "item_category_id")
    private Long id;

    @JoinColumn(name = "item_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Item item;

    @Enumerated(EnumType.STRING)
    private ItemCategoryType itemCategoryType;

    public void setItem(Item item) {
        this.item = item;
    }

    public ItemCategory(ItemCategoryType itemCategoryType) {
        this.itemCategoryType = itemCategoryType;
    }

}
