package com.example.warningmarket.domain.item.dto;

import com.example.warningmarket.domain.item.entity.Item;
import com.example.warningmarket.domain.item.entity.ItemImage;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ItemResponse {

    private String itemName;

    private String sellerName;

    private String sellerCity;

    private String sellerDistrict;

    private String sellerStreetNumber;

    private Double sellerTemperature;

    private String sellerProfileImageUrl;

    private Integer price;

    private String description;

    private List<String> itemCategories;

    private List<String> itemImages;

    private int loveCount;

    private boolean agreeYn;

    private boolean tradeYn;

    public ItemResponse(Item item) {
        this.itemName = item.getItemName();
        this.sellerName = item.getSeller().getUsername();
        this.sellerCity = item.getSeller().getAddress().getCity();
        this.sellerDistrict = item.getSeller().getAddress().getDistrict();
        this.sellerStreetNumber = item.getSeller().getAddress().getStreetNumber();
        this.sellerTemperature = item.getSeller().getTemperature();
        this.sellerProfileImageUrl = item.getSeller().getProfileImageUrl();
        this.price = item.getPrice();
        this.description = item.getDescription();
        this.itemCategories = item.getItemCategories().stream().map(i -> i.getItemCategoryType().getValue()).toList();
        this.itemImages = item.getItemImages().stream().map(ItemImage::getImageUrl).toList();
        this.loveCount = item.getLoves().size();
        this.agreeYn = item.getAgreeYn();
        this.tradeYn = item.getTradeYn();
    }

}
