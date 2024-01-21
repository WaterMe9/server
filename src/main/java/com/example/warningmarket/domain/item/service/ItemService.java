package com.example.warningmarket.domain.item.service;

import com.example.warningmarket.common.exception.ErrorCode;
import com.example.warningmarket.domain.item.repository.ItemRepository;
import com.example.warningmarket.domain.member.exception.MemberErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    public void createItem(){
    }

}
