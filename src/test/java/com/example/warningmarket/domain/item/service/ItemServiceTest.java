package com.example.warningmarket.domain.item.service;

import com.example.warningmarket.common.exception.ApplicationException;
import com.example.warningmarket.common.exception.CommonErrorCode;
import com.example.warningmarket.common.util.S3Util;
import com.example.warningmarket.domain.item.dto.CreateItemRequest;
import com.example.warningmarket.domain.item.dto.ItemResponse;
import com.example.warningmarket.domain.item.dto.ItemSearchCondition;
import com.example.warningmarket.domain.item.dto.ItemSearchListResponse;
import com.example.warningmarket.domain.item.entity.Item;
import com.example.warningmarket.domain.item.entity.ItemCategory;
import com.example.warningmarket.domain.item.entity.ItemCategoryType;
import com.example.warningmarket.domain.item.entity.ItemImage;
import com.example.warningmarket.domain.item.exception.ItemErrorCode;
import com.example.warningmarket.domain.item.repository.ItemRepository;
import com.example.warningmarket.domain.item.repository.LoveRepository;
import com.example.warningmarket.domain.item.repository.dto.ItemSearchQueryDto;
import com.example.warningmarket.domain.member.entity.Address;
import com.example.warningmarket.domain.member.entity.Member;
import com.example.warningmarket.domain.member.repository.MemberRepository;
import com.example.warningmarket.domain.member.service.MemberService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.example.warningmarket.domain.item.MultiPartFileUtil.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Transactional
@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private LoveRepository loveRepository;
    @Mock
    private S3Util s3Util;

    @InjectMocks
    private ItemService itemService;

    private CreateItemRequest createItemRequest() {
        return CreateItemRequest.builder()
                .itemName("선응이형")
                .price(950000)
                .description("선응이형 화이팅")
                .itemCategories(List.of("생활가전"))
                .build();
    }

    private CreateItemRequest updateItemRequest() {
        return CreateItemRequest.builder()
                .itemName("선응이동생")
                .price(950000)
                .description("선응이형 화이팅")
                .itemCategories(List.of("생활가전"))
                .build();
    }

    private ItemSearchCondition itemSearchCondition() {
        return ItemSearchCondition.builder()
                .categories(List.of("생활가전"))
                .order("love")
                .cursor(null)
                .cursorId(null)
                .build();
    }

    private ItemSearchQueryDto itemSearchQueryDto() {
        return ItemSearchQueryDto.builder()
                .item(setItem())
                .member(setMember())
                .itemImageUrl("a")
                .loveCount(0L)
                .build();

    }

    private ItemSearchListResponse itemSearchListResponse() {
        return ItemSearchListResponse.builder()
                .itemList(List.of(itemSearchQueryDto()))
                .cursor(0L)
                .lastItemId(1L)
                .hasNext(false)
                .build();
    }


    private Item setItem() {
        return Item.builder()
                .itemName("선응이형")
                .price(950000)
                .description("선응이형 화이팅")
                .itemImages(List.of(setItemImage()))
                .itemCategories(List.of(setItemCategory()))
                .seller(setMember())
                .build();
    }

    private ItemImage setItemImage() {
        return new ItemImage("a");
    }

    private ItemCategory setItemCategory() {
        return new ItemCategory(ItemCategoryType.APPLIANCES);
    }

    private Member setMember() {
        return Member.builder()
                .username("윤문김")
                .email("aaa123")
                .password("pass123")
                .address(
                        Address.builder()
                                .city("경기도")
                                .district("파주시")
                                .streetNumber("야당로")
                                .build()
                )
                .build();
    }


    @Test
    public void 아이템_생성() throws IOException {

        when(s3Util.upload(any(), any())).thenReturn(List.of("a"));
        MultipartFile image = getMultiPartFile();

        when(itemRepository.save(any())).thenReturn(setItem());
        Item item = itemService.createItem(createItemRequest(), List.of(image), setMember());

        //then
        assertThat(item.getItemName()).isEqualTo(createItemRequest().getItemName());
    }

    @Test
    public void 아이템_생성_실패() {
        ApplicationException e = assertThrows(ApplicationException.class
                , () -> itemService.createItem(createItemRequest(), null, setMember()));
        assertThat(e.getErrorCode()).isEqualTo(ItemErrorCode.NOT_EXIST_IMAGE);
    }

    @Test
    public void 아이템_단건_조회() {
        when(itemRepository.findByIdWithMemberAndItemImages(anyLong())).thenReturn(Optional.of(setItem()));
        ItemResponse itemResponse = itemService.getItem(1L);

        assertThat(itemResponse.getItemName()).isEqualTo(setItem().getItemName());
    }

    @Test
    public void 아이템_전체_조회() {
        when(itemRepository.getItems(itemSearchCondition(), PageRequest.of(0, 10)))
                .thenReturn(itemSearchListResponse());

        ItemSearchListResponse itemList = itemService.getItems(itemSearchCondition(), PageRequest.of(0, 10));

        assertThat(itemList.getItemList().size()).isEqualTo(1);
        assertThat(itemList.getItemList().get(0).getItemName()).isEqualTo("선응이형");
        assertThat(itemList.getCursor()).isEqualTo(0);
        assertThat(itemList.getLastItemId()).isEqualTo(1);
        assertThat(itemList.isHasNext()).isEqualTo(false);
    }

    @Test
    public void 아이템_수정삭제_실패() throws IOException {
        when(itemRepository.findById(any())).thenReturn(Optional.of(setItem()));

        MultipartFile image = getMultiPartFile();

        // 비영속상태라서 주소값 비교는 무조건 실패
        ApplicationException e = assertThrows(ApplicationException.class, () -> itemService.updateItem(1L, updateItemRequest(), List.of(image), setMember()));
        assertThat(e.getErrorCode()).isEqualTo(CommonErrorCode.FORBIDDEN_EXCEPTION);
    }


    @Test
    public void 좋아요() {
        when(itemRepository.findById(any())).thenReturn(Optional.of(setItem()));
        when(loveRepository.findByItemAndMember(any(), any())).thenReturn(Optional.ofNullable(null));

        itemService.loveItem(1L, setMember());
    }

    @Test
    public void 거래상태_변경_실패() {
        when(itemRepository.findById(any())).thenReturn(Optional.of(setItem()));

        ApplicationException e = assertThrows(ApplicationException.class, () -> itemService.updateTradeStatus(1L, setMember()));
        assertThat(e.getErrorCode()).isEqualTo(CommonErrorCode.FORBIDDEN_EXCEPTION);
    }



}
