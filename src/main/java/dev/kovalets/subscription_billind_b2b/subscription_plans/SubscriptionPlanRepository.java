package dev.kovalets.subscription_billind_b2b.subscription_plans;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlanEntity, Long> {
    public List<SubscriptionPlanEntity> findAllByIsActiveTrue();
    public Optional<SubscriptionPlanEntity> findByIdAndIsActiveTrue(Long id);
    Optional<SubscriptionPlanEntity> findByIdAndTenantIdAndIsActiveTrue(Long id, Long tenantId);
}
