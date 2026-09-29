package dev.kovalets.subscription_billind_b2b.subscriptions.dto;

import dev.kovalets.subscription_billind_b2b.subscriptions.SubscriptionStatus;

import java.time.OffsetDateTime;

public record SubscriptionDto(
        Long id,
        Long subscriptionPlanId,
        Long customerId,
        OffsetDateTime startDate,
        OffsetDateTime nextBillingDate,
        SubscriptionStatus status,
        Long tenantId
) {
    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", subscriptionPlanId=" + subscriptionPlanId +
                ", customerId=" + customerId +
                ", startDate=" + startDate +
                ", nextBillingDate=" + nextBillingDate +
                ", status=" + status +
                ", tenantId=" + tenantId +
                '}';
    }
}
