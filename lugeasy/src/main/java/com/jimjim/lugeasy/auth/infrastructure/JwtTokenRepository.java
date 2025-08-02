package com.jimjim.lugeasy.auth.infrastructure;

import com.jimjim.lugeasy.auth.domain.JwtToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JwtTokenRepository extends CrudRepository<JwtToken, Long> {
    Optional<JwtToken> findById(Long id);

    Optional<JwtToken> findByAccessToken(String accessToken);

    void deleteById(Long Id);
}
