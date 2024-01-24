package com.example.warningmarket.domain.item.repository;

import com.example.warningmarket.domain.item.entity.Item;
import com.example.warningmarket.domain.member.entity.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long>, ItemRepositoryCustom {

    @Query("select distinct i from Item i join fetch i.seller join fetch i.itemImages where i.id = :id")
    Optional<Item> findByIdWithMemberAndItemImages(@Param("id") Long id);
}
