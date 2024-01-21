package com.example.warningmarket.domain.item.entity;

import com.example.warningmarket.common.entity.BaseEntity;
import com.example.warningmarket.common.entity.BooleanToYNConverter;
import com.example.warningmarket.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    @JoinColumn(name = "seller_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member seller;

    private String itemName;

    private int price;

    private String description;

    @Convert(converter = ItemCategoryConvertor.class)
    private List<ItemCategory> itemCategories;

    @Convert(converter = ItemImageUrlConverter.class)
    private List<String> itemImageUrls;

    @Convert(converter = BooleanToYNConverter.class)
    private Boolean agreeYn;

    @Convert(converter = BooleanToYNConverter.class)
    private Boolean tradeYn;

    @Builder
    public Item(Member seller, String itemName, int price, String description, List<ItemCategory> itemCategories, List<String> itemImageUrls) {
        this.seller = seller;
        this.itemName = itemName;
        this.price = price;
        this.description = description;
        this.itemCategories = itemCategories;
        this.itemImageUrls = itemImageUrls;
        this.agreeYn = false;
        this.tradeYn = false;
    }

}
