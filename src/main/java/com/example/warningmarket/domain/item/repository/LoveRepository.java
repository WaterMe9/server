package com.example.warningmarket.domain.item.repository;

import com.example.warningmarket.domain.item.entity.Item;
import com.example.warningmarket.domain.item.entity.Love;
import com.example.warningmarket.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LoveRepository extends JpaRepository<Love, Long> {

    Optional<Love> findByItemAndMember(Item item, Member member);

}
