package org.emocare.emocare.repository;

import org.emocare.emocare.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Integer>
{
    Optional<Token> findByToken(String aInToken);
}