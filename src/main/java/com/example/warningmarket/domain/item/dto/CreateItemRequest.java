package com.example.warningmarket.domain.item.dto;

import lombok.Data;

import java.util.List;

@Data
public class CreateItemRequest {

    private String itemName;
    private int price;
    private String description;
    private List<String> itemCategories;

}
