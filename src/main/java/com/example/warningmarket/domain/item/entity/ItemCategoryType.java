package com.example.warningmarket.domain.item.entity;

import lombok.Getter;

@Getter
public enum ItemCategoryType {

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

    private ItemCategoryType(String value) {
        this.value = value;
    }

    public static ItemCategoryType valueToEnum(String value) {
        for (ItemCategoryType category : ItemCategoryType.values()) {
            if (category.getValue().equals(value)) {
                return category;
            }
        }
        return null;
    }

}
