package com.example.warningmarket.domain.item.controller;

import com.example.warningmarket.common.AbstractRestDocsTests;
import com.example.warningmarket.domain.item.MultiPartFileUtil;
import com.example.warningmarket.domain.item.dto.CreateItemRequest;
import com.example.warningmarket.domain.item.entity.Item;
import com.example.warningmarket.domain.item.repository.ItemRepository;
import com.example.warningmarket.domain.item.service.ItemService;
import com.example.warningmarket.domain.member.dto.SignInRequest;
import com.example.warningmarket.domain.member.dto.SignUpRequest;
import com.example.warningmarket.domain.member.dto.TokenDto;
import com.example.warningmarket.domain.member.entity.Member;
import com.example.warningmarket.domain.member.repository.MemberRepository;
import com.example.warningmarket.domain.member.service.MemberService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.example.warningmarket.domain.item.MultiPartFileUtil.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

public class ItemControllerTest extends AbstractRestDocsTests {

    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ItemService itemService;
    @Autowired
    private ItemRepository itemRepository;

    private String accessToken;
    private String itemId;
    private static final String email = "test@test.com";

    @BeforeEach
    public void before() throws IOException {
        // 로그인
        login();

        // 아이템 생성
        createItem();
    }

    private void login() {
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .username("테스트유저")
                .email(email)
                .password("123")
                .passwordCheck("123")
                .city("파주시")
                .district("금촌동")
                .streetNumber("란란로 30")
                .build();
        memberService.signUp(signUpRequest);

        SignInRequest signInRequest = SignInRequest.builder()
                .email(email)
                .password("123")
                .build();
        TokenDto tokenDto = memberService.signIn(signInRequest);
        accessToken = tokenDto.getAccessToken();
    }


    private void createItem() throws IOException {
        MultipartFile image = getMultiPartFile();
        Member member = memberRepository.findByEmail(email).orElseThrow();
        Item item = itemService.createItem(createItemRequest(), List.of(image), member);
        itemId = item.getId().toString();

        for (int i = 0; i < 10; i++) {
            itemService.createItem(createItemRequest(), List.of(image), member);
        }
        // 총 11개의 아이템 생성
    }



    private CreateItemRequest createItemRequest() {
        return CreateItemRequest.builder()
                .itemName("선응이형")
                .price(950000)
                .description("선응이형 화이팅")
                .itemCategories(List.of("디지털기기", "유아동"))
                .build();
    }


    private MockHttpServletRequestBuilder createMultiPartRequest(
            MockMultipartHttpServletRequestBuilder multipartRequest,
            CreateItemRequest createItemRequest
    ) throws IOException {

        // 이미지 추가
        final MockMultipartFile image = getMultiPartFile();

        multipartRequest.file(image);
        multipartRequest.file(image);

        MockMultipartFile itemMockRequest = new MockMultipartFile("item_info", ""
                , "application/json", objectMapper.writeValueAsString(createItemRequest()).getBytes(StandardCharsets.UTF_8));

        // dto 추가
        multipartRequest.file(itemMockRequest);

        // 헤더에 토큰 추가
        multipartRequest.header("Authorization", accessToken);

        return multipartRequest;
    }


    @Test
    public void 아이템_생성() throws Exception {
        MockMultipartHttpServletRequestBuilder multipartRequest = multipart(HttpMethod.POST, "/api/item");
        mockMvc.perform(
                createMultiPartRequest(multipartRequest, createItemRequest())
        ).andExpect(status().isOk());
    }


    @Test
    public void 아이템_단건_조회() throws Exception {
        mockMvc.perform(get("/api/item/"+itemId)
                        .header("Authorization", accessToken)
                )
                .andExpect(status().isOk())
                .andDo(
                        document("item-controller-test/아이템_단건_조회",
                                responseFields(
                                        beneathPath("data").withSubsectionId("data"),
                                        fieldWithPath("item_name").description("상품명"),
                                        fieldWithPath("seller_name").description("판매자명"),
                                        fieldWithPath("seller_city").description("판매자 주소(시)"),
                                        fieldWithPath("seller_district").description("판매자 주소(동)"),
                                        fieldWithPath("seller_street_number").description("판매자 주소(도로명)"),
                                        fieldWithPath("seller_temperature").description("판매자 신뢰온도"),
                                        fieldWithPath("seller_profile_image_url").description("판매자 프로필사진"),
                                        fieldWithPath("price").description("상품가격"),
                                        fieldWithPath("description").description("상세설명"),
                                        fieldWithPath("item_categories").type(JsonFieldType.ARRAY).description("상품 카테고리 리스트"),
                                        fieldWithPath("item_images").type(JsonFieldType.ARRAY).description("상품 사진 리스트"),
                                        fieldWithPath("love_count").description("좋아요수"),
                                        fieldWithPath("agree_yn").description("가격합의가능여부"),
                                        fieldWithPath("trade_yn").description("거래중 여부")
                                )
                        )
                );
    }

    @Test
    public void 아이템_전체_조회() throws Exception {
        mockMvc.perform(
                get("/api/item")
                        .param("categories","")
                        .param("keyword","")
                        .param("order","")
                        .param("cursorId","")
                        .param("cursor","")
                        .header("Authorization", accessToken)
        ).andExpect(status().isOk())
                .andDo(
                        document(
                                "item-controller-test/아이템_전체_조회",
                                responseFields(
                                        beneathPath("data").withSubsectionId("data"),
                                        fieldWithPath("item_list").description("아이템 리스트"),
                                        fieldWithPath("item_list[].item_id").description("아이템 id"),
                                        fieldWithPath("item_list[].item_name").description("상품명"),
                                        fieldWithPath("item_list[].seller_name").description("판매자명"),
                                        fieldWithPath("item_list[].seller_city").description("판매자 주소(시)"),
                                        fieldWithPath("item_list[].seller_district").description("판매자 주소(동)"),
                                        fieldWithPath("item_list[].seller_temperature").description("판매자 신뢰온도"),
                                        fieldWithPath("item_list[].seller_profile_image_url").description("판매자 프로필사진"),
                                        fieldWithPath("item_list[].price").description("상품가격"),
                                        fieldWithPath("item_list[].item_image").description("상품 사진(대표 이미지)"),
                                        fieldWithPath("item_list[].love_count").description("좋아요 개수"),
                                        fieldWithPath("item_list[].agree_yn").description("가격합의가능여부"),
                                        fieldWithPath("item_list[].trade_yn").description("거래중 여부"),
                                        fieldWithPath("has_next").description("다음 페이지 존재 여부"),
                                        fieldWithPath("cursor").description("최신순 : null, 좋아요순 : 마지막 아이템의 좋아요 개수"),
                                        fieldWithPath("last_item_id").description("마지막 아이템의 id(최신순, 좋아요순 둘 다 해당)")
                                )
                        )
                );
    }

    @Test
    public void 아이템_수정() throws Exception {
        MockMultipartHttpServletRequestBuilder multipartRequest = multipart(HttpMethod.PUT, "/api/item/"+itemId);
        mockMvc.perform(
                createMultiPartRequest(multipartRequest, createItemRequest())
        ).andExpect(status().isOk());
    }

    @Test
    public void 아이템_삭제() throws Exception {
        mockMvc.perform(
                delete("/api/item/" + itemId)
                        .header("Authorization", accessToken)
        ).andExpect(status().isOk());
    }


    @Test
    public void 아이템_좋아요() throws Exception {
        mockMvc.perform(
                post("/api/item/" + itemId+"/love")
                        .header("Authorization", accessToken)
        ).andExpect(status().isOk());
    }

    @Test
    public void 아이템_거래상태_변경() throws Exception {
        mockMvc.perform(
                post("/api/item/" + itemId+"/trade")
                        .header("Authorization", accessToken)
        ).andExpect(status().isOk());
    }

}
