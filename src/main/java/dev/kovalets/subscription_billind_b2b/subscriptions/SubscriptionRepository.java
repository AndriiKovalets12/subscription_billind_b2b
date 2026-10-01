package dev.kovalets.subscription_billind_b2b.subscriptions;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<SubscriptionEntity, Long> {

    List<SubscriptionEntity> findAllByStatus(SubscriptionStatus status);

    Optional<SubscriptionEntity> findByIdAndTenantId(Long id, Long tenantId);

    @Query("SELECT s FROM SubscriptionEntity s JOIN FETCH s.customer WHERE s.status = :status AND s.nextBillingDate <= :date")
    Page<SubscriptionEntity> findActiveSubscriptionsByBilling(
            @Param("date")OffsetDateTime date,
            @Param("status") SubscriptionStatus status,
            Pageable pageable);

    @Query("SELECT s FROM SubscriptionEntity s JOIN FETCH s.customer WHERE s.status = :status AND s.nextRetryDate <= :date")
    Page<SubscriptionEntity> findPastDueSubscriptionsByBilling(
            @Param("date")OffsetDateTime date,
            @Param("status") SubscriptionStatus status,
            Pageable pageable);

}