package com.example.warningmarket.domain.item.controller;

import com.example.warningmarket.common.annotation.Auth;
import com.example.warningmarket.common.response.ApplicationResponse;
import com.example.warningmarket.domain.item.dto.CreateItemRequest;
import com.example.warningmarket.domain.item.dto.ItemResponse;
import com.example.warningmarket.domain.item.dto.ItemSearchCondition;
import com.example.warningmarket.domain.item.dto.ItemSearchListResponse;
import com.example.warningmarket.domain.item.service.ItemService;
import com.example.warningmarket.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequestMapping("/api/item")
@RestController
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping()
    public ResponseEntity<ApplicationResponse<?>> createItem(@Auth Member member
            , @Validated @RequestPart("item_info") CreateItemRequest createItemRequest
            , @RequestPart(value = "item_images", required = false) List<MultipartFile> images) {
        itemService.createItem(createItemRequest, images, member);
        return ResponseEntity.ok(new ApplicationResponse<>());
    }

    @GetMapping("{item_id}")
    public ResponseEntity<ApplicationResponse<?>> getItem(@PathVariable("item_id") Long id) {
        ItemResponse itemResponse = itemService.getItem(id);
        return ResponseEntity.ok(new ApplicationResponse<ItemResponse>(itemResponse));
    }

    @GetMapping()
    public ResponseEntity<ApplicationResponse<?>> getItems(Pageable pageable
                                                          , @Validated @ModelAttribute ItemSearchCondition condition) {
        ItemSearchListResponse itemListResponse = itemService.getItems(condition, pageable);
        return ResponseEntity.ok(new ApplicationResponse<ItemSearchListResponse>(itemListResponse));
    }

    @DeleteMapping("{item_id}")
    public ResponseEntity<ApplicationResponse<?>> deleteItem(@Auth Member member
                                                           , @PathVariable("item_id") Long id) {
        itemService.deleteItem(id, member);
        return ResponseEntity.ok(new ApplicationResponse<>());
    }

    @PutMapping("{item_id}")
    public ResponseEntity<ApplicationResponse<?>> updateItem(@Auth Member member
                                                           , @PathVariable("item_id") Long id
                                                           , @Validated @RequestPart("item_info") CreateItemRequest createItemRequest
                                                           , @RequestPart(value = "item_images", required = false) List<MultipartFile> images){
        itemService.updateItem(id, createItemRequest, images, member);
        return ResponseEntity.ok(new ApplicationResponse<>());
    }

    @PostMapping("{item_id}/love")
    public ResponseEntity<ApplicationResponse<?>> loveItem(@Auth Member member
                                                          , @PathVariable("item_id") Long id) {
        itemService.loveItem(id,member);
        return ResponseEntity.ok(new ApplicationResponse<>());
    }

    @PostMapping("{item_id}/trade")
    public ResponseEntity<ApplicationResponse<?>> updateTradeStatus(@Auth Member member
                                                           , @PathVariable("item_id") Long id) {
        itemService.updateTradeStatus(id, member);
        return ResponseEntity.ok(new ApplicationResponse<>());
    }
}
