package dev.kovalets.subscription_billind_b2b.subscriptions;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<SubscriptionEntity, Long> {

    List<SubscriptionEntity> findAllByStatus(SubscriptionStatus status);

    Optional<SubscriptionEntity> findByIdAndTenantId(Long id, Long tenantId);
}