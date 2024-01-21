package com.example.warningmarket.domain.item.entity;

import lombok.Getter;

@Getter
public enum ItemCategory {

    DEVICE("디지털기기"),
    APPLIANCES("생활가전"),
    FURNITURE("가구"),
    KITCHEN("주방"),
    INFANT("유아동"),
    CLOTHES("의류"),
    SPORTS("스포츠"),
    HOBBY("취미"),
    USED_CAR("중고차"),
    BOOK("도서"),
    TICKET("티켓"),
    PET("동물용품");

    private final String value;

    private ItemCategory(String value) {
        this.value = value;
    }

    public static String valueToEnum(String value) {
        for (ItemCategory category : ItemCategory.values()) {
            if (category.getValue().equals(value)) {
                return category.toString();
            }
        }
        return null;
    }

    public static ItemCategory stringToEnum(String value) {
        for (ItemCategory category : ItemCategory.values()) {
            if (category.toString().equals(value)) {
                return category;
            }
        }
        return null;
    }

}
