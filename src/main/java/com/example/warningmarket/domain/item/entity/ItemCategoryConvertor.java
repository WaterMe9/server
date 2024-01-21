package com.example.warningmarket.domain.item.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.List;

@Converter
public class ItemCategoryConvertor implements AttributeConverter<List<ItemCategory>, String> {
    @Override
    public String convertToDatabaseColumn(List<ItemCategory> attribute) {
        return attribute.isEmpty()? null: String.join(",",attribute.toString());
    }

    @Override
    public List<ItemCategory> convertToEntityAttribute(String dbData) {
        return Arrays.stream(dbData.split(",")).map(ItemCategory::stringToEnum).toList();
    }
}
