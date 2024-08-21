package io.spring.orderservice.member.repository;

import io.spring.orderservice.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByEmail(String email);
    Member findByNickname(String nickname);
}
