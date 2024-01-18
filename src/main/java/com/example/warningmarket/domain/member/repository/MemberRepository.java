package com.example.warningmarket.domain.member.repository;

import com.example.warningmarket.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
