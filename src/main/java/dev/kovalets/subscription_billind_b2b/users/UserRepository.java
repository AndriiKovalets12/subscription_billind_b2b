package dev.kovalets.subscription_billind_b2b.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query("SELECT u FROM UserEntity u WHERE u.isActive=true")
    List<UserEntity> findAllActive();

    @Query("SELECT u FROM UserEntity u WHERE u.isActive=true AND u.id = :id")
    Optional<UserEntity> findActiveById(@Param("id") Long id);

    boolean existsByEmailAndIsActiveTrue(String email);
}
