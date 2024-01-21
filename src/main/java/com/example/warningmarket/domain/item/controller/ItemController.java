package com.example.warningmarket.domain.item.controller;

import com.example.warningmarket.common.response.ApplicationResponse;
import com.example.warningmarket.domain.item.dto.CreateItemRequest;
import com.example.warningmarket.domain.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequestMapping("/item")
@RestController
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping()
    public ResponseEntity<ApplicationResponse<?>> createItem(@RequestPart("itemInfo") CreateItemRequest createItemRequest
                                                            , @RequestPart("itemImages") List<MultipartFile> images) {
        return null;
    }
}
