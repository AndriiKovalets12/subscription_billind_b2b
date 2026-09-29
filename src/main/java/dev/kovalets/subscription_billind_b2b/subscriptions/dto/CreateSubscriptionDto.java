package dev.kovalets.subscription_billind_b2b.subscriptions.dto;

import dev.kovalets.subscription_billind_b2b.subscriptions.SubscriptionStatus;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import org.aspectj.lang.annotation.After;

import java.time.OffsetDateTime;

public record CreateSubscriptionDto(
        @NotNull(message = "Subscription plan id cannot be empty or null.")
        @Positive(message = "Subscription plan id must be greater than zero.")
        Long subscriptionPlanId,

        @NotNull(message = "Customer's id cannot be empty or null.")
        @Positive(message = "Customer's id must be greater than zero.")
        Long customerId,

        @Future
        OffsetDateTime nextBillingDate,

        @NotNull(message = "Tenant's id cannot be empty or null.")
        @Positive(message = "Tenant's id must be greater than zero.")
        Long tenantId
) {
    @Override
    public String toString() {
        return "{" +
                "subscriptionPlanId=" + subscriptionPlanId +
                ", customerId=" + customerId +
                ", nextBillingDate=" + nextBillingDate +
                ", tenantId=" + tenantId +
                '}';
    }
}
