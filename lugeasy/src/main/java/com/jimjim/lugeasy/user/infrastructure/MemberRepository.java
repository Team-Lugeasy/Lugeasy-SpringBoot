package com.jimjim.lugeasy.user.infrastructure;

import com.jimjim.lugeasy.user.domain.Member;
import com.jimjim.lugeasy.user.domain.SocialType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    
    // 소셜 로그인으로 회원 찾기
    Optional<Member> findBySocialIdAndSocialType(String socialId, SocialType socialType);
    
    // 이메일로 회원 찾기
    Optional<Member> findByEmail(String email);
    
    // 닉네임으로 회원 찾기
    Optional<Member> findByName(String name);
}
