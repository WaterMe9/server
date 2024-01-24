package com.example.warningmarket.domain.item.dto;

import com.example.warningmarket.domain.item.repository.dto.ItemSearchQueryDto;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ItemSearchListResponse {

    private List<ItemSearchQueryDto> itemList;
    private boolean hasNext;
    private Long cursor;  // 좋아요순 일때만 필요
    private Long lastItemId;

}
