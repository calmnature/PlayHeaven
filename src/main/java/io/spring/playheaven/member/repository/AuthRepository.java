package io.spring.playheaven.member.repository;

import io.spring.playheaven.member.entity.Auth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthRepository extends JpaRepository<Auth, Long> {
    Auth findByEmail(String email);
}
