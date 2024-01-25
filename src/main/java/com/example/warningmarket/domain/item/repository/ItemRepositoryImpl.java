package com.example.warningmarket.domain.item.repository;

import com.example.warningmarket.domain.item.dto.ItemSearchCondition;
import com.example.warningmarket.domain.item.dto.ItemSearchListResponse;
import com.example.warningmarket.domain.item.entity.ItemCategoryType;
import com.example.warningmarket.domain.item.entity.QItemImage;
import com.example.warningmarket.domain.item.repository.dto.ItemSearchQueryDto;
import com.example.warningmarket.domain.item.repository.dto.QItemSearchQueryDto;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.example.warningmarket.domain.item.entity.QItem.*;
import static com.example.warningmarket.domain.item.entity.QItemCategory.*;
import static com.example.warningmarket.domain.item.entity.QItemImage.*;
import static com.example.warningmarket.domain.item.entity.QLove.*;
import static com.example.warningmarket.domain.member.entity.QMember.*;

@Slf4j
@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public ItemSearchListResponse getItems(ItemSearchCondition condition, Pageable pageable) {

        QItemImage itemImageSub = new QItemImage("itemImageSub");
        List<ItemSearchQueryDto> result = jpaQueryFactory
                .select(new QItemSearchQueryDto(item, member, love.count()
                        , ExpressionUtils.as(
                                JPAExpressions.select(itemImage.imageUrl)
                                        .from(itemImage)
                                        .where(itemImage.id.eq(
                                                JPAExpressions
                                                        .select(itemImageSub.id.min())
                                                        .from(itemImageSub)
                                                        .where(itemImageSub.item.id.eq(item.id))
                                        )), "itemImageUrl")
                ))
                .distinct()
                .from(item)
                .join(item.seller, member).fetchJoin()
                .leftJoin(item.loves, love)
                .join(item.itemCategories, itemCategory)
                .where(cursorFilter(condition.getCursor(), condition.getCursorId(), condition.getOrder()), searchFiler(condition.getKeyword()), categoryFilter(condition.getCategories()))
                .groupBy(item.id)
                .orderBy(orderSpecifier(condition.getOrder()), item.id.asc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return checkLastPage(result, pageable, condition.getOrder());
    }


    /*
    마지막 페이지 체크
     */
    private ItemSearchListResponse checkLastPage(List<ItemSearchQueryDto> result, Pageable pageable, String order) {
        boolean hasNext = false;

        if (result.isEmpty()) {
            return new ItemSearchListResponse(result, hasNext, 0L, 0L);
        }

        if(result.size() > pageable.getPageSize()) {
            hasNext = true;
            result.remove(pageable.getPageSize());
        }

        Long cursor = null;
        Long lastItemId = result.get(result.size()-1).getItemId();

        if (order.equals("love")){
            cursor = result.get(result.size()-1).getLoveCount();
        }

        return new ItemSearchListResponse(result, hasNext, cursor, lastItemId);

    }


    /*
    좋아요순 : 좋아요수, itemId 비교
    최신순 : itemId 비교
     */
    private BooleanExpression cursorFilter(Long cursor, Long cursorId, String order) {
        if (cursor == null && cursorId == null) { // 첫번째 페이지인 경우
            return null;
        } else if (order.equals("love")){  // 좋아요순 인 경우
            if (cursor == 0) {  // count가 0일경우
                return item.id.gt(cursorId);
            }
            return love.count().lt(cursor)
                    .or(love.count().eq(cursor)
                            .and(item.id.gt(cursorId))
                    );
        } else if (order.equals("recent")){  // 최신순 인 경우
            return item.id.lt(cursorId);
        }
        // 디폴트 (최신순)
        return item.id.lt(cursorId);
    }

    private BooleanExpression searchFiler(String keyword) {
        if (keyword != null) {
            return item.itemName.contains(keyword).or(item.description.contains(keyword));
        }
        return null;
    }

    private BooleanExpression categoryFilter(List<String> categories) {
        if (categories.isEmpty()) {
            return null;
        } else {
            List<ItemCategoryType> categoryTypes = categories.stream().map(ItemCategoryType::valueToEnum).toList();
            return itemCategory.itemCategoryType.in(categoryTypes);
        }
    }

    private OrderSpecifier orderSpecifier(String order) {
        if (order == null) {
            return item.updateAt.desc();
        } else if (order.equals("love")){
            return love.count().desc();
        } else if (order.equals("recent")){
            return item.updateAt.desc();
        }
        return item.updateAt.desc();
    }

}
