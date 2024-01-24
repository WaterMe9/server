package com.example.warningmarket.domain.item.repository;

import com.example.warningmarket.domain.item.dto.ItemSearchCondition;
import com.example.warningmarket.domain.item.dto.ItemSearchListResponse;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom{

    ItemSearchListResponse getItems(ItemSearchCondition condition, Pageable pageable);

}
