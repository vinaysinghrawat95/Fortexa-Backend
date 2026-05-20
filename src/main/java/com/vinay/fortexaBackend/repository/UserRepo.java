package com.vinay.fortexaBackend.repository;

import com.vinay.fortexaBackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;


@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    Optional<User>findByUsernameAndDeletedAtIsNull(String username);
}
