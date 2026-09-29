package dev.kovalets.subscription_billind_b2b.tenants;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TenantRepository extends JpaRepository<TenantEntity, Long> {
    boolean existsByName(String name);
}
