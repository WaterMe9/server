package com.example.warningmarket.domain.item.service;

import com.example.warningmarket.common.exception.ApplicationException;
import com.example.warningmarket.common.exception.CommonErrorCode;
import com.example.warningmarket.common.util.S3Util;
import com.example.warningmarket.domain.item.dto.CreateItemRequest;
import com.example.warningmarket.domain.item.dto.ItemResponse;
import com.example.warningmarket.domain.item.entity.*;
import com.example.warningmarket.domain.item.repository.ItemRepository;
import com.example.warningmarket.domain.item.repository.LoveRepository;
import com.example.warningmarket.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static com.example.warningmarket.common.exception.CommonErrorCode.*;
import static com.example.warningmarket.domain.item.exception.ItemErrorCode.*;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final LoveRepository loveRepository;
    private final S3Util s3Util;

    /*
    item 저장
     */
    @Transactional
    public void createItem(CreateItemRequest request, List<MultipartFile> images, Member member){
        // 이미지 존재 여부 체크
        if (images.isEmpty()) {
            throw new ApplicationException(NOT_EXIST_IMAGE);
        }

        // 이미지 업로드
        List<String> imageUrls = s3Util.upload(images, "item");

        // ItemImage 생성
        List<ItemImage> itemImages = generateItemImage(imageUrls);

        // ItemCategory 생성
        List<ItemCategory> itemCategories = generateItemCategory(request.getItemCategories());


        // Item 생성
        Item item = Item.builder()
                .itemName(request.getItemName())
                .price(request.getPrice())
                .description(request.getDescription())
                .itemImages(itemImages)
                .itemCategories(itemCategories)
                .seller(member)
                .build();

        // Item & ItemImage & ItemCategory 저장
        itemRepository.save(item);
    }

    /*
    item 단건 조회
     */
    public ItemResponse getItem(Long itemId) {
        Item item = itemRepository.findByIdWithMemberAndItemImages(itemId).orElseThrow(
                () -> new ApplicationException(NOT_EXIST_ITEM)
        );
        return new ItemResponse(item);
    }

    /*
    item 리스트 조회
     */
    public void getItems(Long itemId) {

    }

    /*
    item 삭제
     */
    @Transactional
    public void deleteItem(Long itemId, Member member) {
        // 검증
        Item item = validateMember(itemId, member);

        // Item 삭제
        itemRepository.delete(item);
    }

    /*
    item 수정 : s3에서 기존 이미지 삭제 처리는 아직x
     */
    @Transactional
    public void updateItem(Long itemId, CreateItemRequest request, List<MultipartFile> images, Member member) {
        // 검증
        Item item = validateMember(itemId, member);

        // 이미지 업로드
        List<String> imageUrls = s3Util.upload(images, "item");

        // ItemImage 생성
        List<ItemImage> itemImages = generateItemImage(imageUrls);

        // ItemCategory 생성
        List<ItemCategory> itemCategories = generateItemCategory(request.getItemCategories());

        // Item 수정
        item.updateItem(request.getItemName(), request.getPrice(), request.getDescription(), itemCategories, itemImages);
    }

    /*
    상품에 대한 좋아요 기능
     */
    @Transactional
    public void loveItem(Long itemId, Member member) {
        Item item = itemRepository.findById(itemId).orElseThrow(
                () -> new ApplicationException(NOT_EXIST_ITEM)
        );
        Optional<Love> findLove = loveRepository.findByItemAndMember(item, member);

        // 좋아요 있으면 취소, 없으면 좋아요
        if (findLove.isPresent()) {
            loveRepository.delete(findLove.get());
        } else {
            Love love = Love.builder().item(item).member(member).build();
            loveRepository.save(love);
        }
    }

    /*
    ItemCategory 생성
     */
    private List<ItemCategory> generateItemCategory(List<String> itemCategories){
        // ImageCategory Enum 화
        List<ItemCategoryType> itemCategoryTypes = itemCategories
                .stream()
                .map(ItemCategoryType::valueToEnum)
                .toList();
        return itemCategoryTypes
                .stream()
                .map(ItemCategory::new)
                .toList();
    }

    // ItemImage 생성
    private List<ItemImage> generateItemImage(List<String> itemImageUrls) {
        return itemImageUrls
                .stream()
                .map(ItemImage::new)
                .toList();
    }

    // 수정 or 삭제 시, 검증
    private Item validateMember(Long itemId, Member member) {
        Item item = itemRepository.findById(itemId).orElseThrow(
                () -> new ApplicationException(NOT_EXIST_ITEM)
        );
        if (item.getSeller() != member) {
            throw new ApplicationException(FORBIDDEN_EXCEPTION);
        }
        return item;
    }

}
