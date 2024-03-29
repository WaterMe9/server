package com.example.warningmarket.domain.item.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemSearchCondition {

    private List<String> categories;
    private String keyword;
    private String order;
    private Long cursorId; // 최신순, 좋아요순 커서
    private Long cursor; // 좋아요순 커서 (최신순은 Null)

}
