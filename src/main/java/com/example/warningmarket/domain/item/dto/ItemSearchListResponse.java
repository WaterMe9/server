package com.example.warningmarket.domain.item.dto;

import com.example.warningmarket.domain.item.repository.dto.ItemSearchQueryDto;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ItemSearchListResponse {

    private List<ItemSearchQueryDto> itemList;
    private boolean hasNext;
    private Long cursor;  // 좋아요순 일때만 필요
    private Long lastItemId;

}
