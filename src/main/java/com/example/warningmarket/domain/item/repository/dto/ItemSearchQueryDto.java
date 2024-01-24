package com.example.warningmarket.domain.item.repository.dto;

import com.example.warningmarket.domain.item.entity.Item;
import com.example.warningmarket.domain.member.entity.Member;
import com.querydsl.core.annotations.QueryProjection;
import com.querydsl.core.types.Expression;
import lombok.Data;

@Data
public class ItemSearchQueryDto {

    private Long itemId;

    private String itemName;

    private String sellerName;

    private String sellerCity;

    private String sellerDistrict;

    private Double sellerTemperature;

    private String sellerProfileImageUrl;

    private Integer price;

    private String itemImage;

    private Long loveCount;

    private boolean agreeYn;

    private boolean tradeYn;

    @QueryProjection
    public ItemSearchQueryDto(Item item, Member member, Long loveCount, String itemImageUrl) {
        this.itemId = item.getId();
        this.itemName = item.getItemName();
        this.sellerName = member.getUsername();
        this.sellerCity = member.getAddress().getCity();
        this.sellerDistrict = member.getAddress().getDistrict();
        this.sellerTemperature = member.getTemperature();
        this.sellerProfileImageUrl = member.getProfileImageUrl();
        this.price = item.getPrice();
        this.itemImage = itemImageUrl;
        this.loveCount = loveCount;
        this.agreeYn = item.getAgreeYn();
        this.tradeYn = item.getTradeYn();
    }
}
