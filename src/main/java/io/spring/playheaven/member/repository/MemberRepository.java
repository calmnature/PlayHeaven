package io.spring.playheaven.member.repository;

import io.spring.playheaven.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByEmail(String email);
    Member findByNickname(String nickname);
}
