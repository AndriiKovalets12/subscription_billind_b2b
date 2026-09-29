package dev.kovalets.subscription_billind_b2b.customers;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {
    Optional<CustomerEntity> findByExternalCustomerIdAndTenantId(String externalCustomerId, Long tenantId);

    @Query("SELECT c FROM CustomerEntity c WHERE c.isActive = true")
    List<CustomerEntity> findAllActive();

    @Query("SELECT c FROM CustomerEntity c WHERE c.isActive = true AND c.id = :id LIMIT 1")
    Optional<CustomerEntity> findActiveById(Long id);

    Optional<CustomerEntity> findByIdAndTenantIdAndIsActiveTrue(Long id, Long tenantId);
}
