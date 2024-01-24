package com.example.warningmarket.domain.item.entity;

import com.example.warningmarket.common.entity.BaseEntity;
import com.example.warningmarket.common.entity.BooleanToYNConverter;
import com.example.warningmarket.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
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

    @Convert(converter = BooleanToYNConverter.class)
    private Boolean agreeYn;

    @Convert(converter = BooleanToYNConverter.class)
    private Boolean tradeYn;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemImage> itemImages = new ArrayList<>();

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemCategory> itemCategories = new ArrayList<>();

    @OneToMany(mappedBy = "item")
    private List<Love> loves = new ArrayList<>();

    private void setItemImages(List<ItemImage> itemImages){
        for (ItemImage itemImage : itemImages) {
            this.itemImages.add(itemImage);
            itemImage.setItem(this);
        }
    }

    private void setItemCategories(List<ItemCategory> itemCategories) {
        for (ItemCategory itemCategory : itemCategories) {
            this.itemCategories.add(itemCategory);
            itemCategory.setItem(this);
        }
    }

    @Builder
    public Item(Member seller, String itemName, int price, String description, List<ItemCategory> itemCategories, List<ItemImage> itemImages) {
        this.seller = seller;
        this.itemName = itemName;
        this.price = price;
        this.description = description;
        this.agreeYn = false;
        this.tradeYn = false;
        setItemCategories(itemCategories);
        setItemImages(itemImages);
    }

    public void updateItem(String itemName, int price, String description, List<ItemCategory> itemUpdateCategories, List<ItemImage> itemUpdateImages) {
        this.itemName = itemName;
        this.price = price;
        this.description = description;
        updateCategory(itemUpdateCategories);
        updateImages(itemUpdateImages);
    }

    public void updateCategory(List<ItemCategory> itemUpdateCategories) {
        this.itemCategories.clear();
        setItemCategories(itemUpdateCategories);
    }

    public void updateImages(List<ItemImage> itemUpdateImages) {
        this.itemImages.clear();
        setItemImages(itemUpdateImages);
    }

}
