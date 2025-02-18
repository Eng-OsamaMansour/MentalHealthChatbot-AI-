package org.emocare.emocare.repository;

import org.emocare.emocare.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String>
{
    @Override
    Optional<User> findById(String id);

    Optional<User> findByUsername(String username);
}
