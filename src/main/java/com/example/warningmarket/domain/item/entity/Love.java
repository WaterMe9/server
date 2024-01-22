package com.example.warningmarket.domain.item.entity;

import com.example.warningmarket.domain.item.entity.Item;
import com.example.warningmarket.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Love {

    @Id
    @GeneratedValue
    @Column(name = "love_id")
    private Long id;

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @JoinColumn(name = "item_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Item item;

    private void setMember(Member member) {
        this.member = member;
        member.getLoves().add(this);
    }

    private void setItem(Item item) {
        this.item = item;
        item.getLoves().add(this);
    }
    @Builder
    public Love(Member member, Item item) {
        setMember(member);
        setItem(item);
    }
}
