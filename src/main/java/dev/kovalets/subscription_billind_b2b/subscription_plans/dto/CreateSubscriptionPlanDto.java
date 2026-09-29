package dev.kovalets.subscription_billind_b2b.subscription_plans.dto;

import dev.kovalets.subscription_billind_b2b.subscription_plans.BillingCycle;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record CreateSubscriptionPlanDto(
        @NotBlank(message = "Name cannot be empty.")
        @Size(max = 25, message = "Name is too long.")
        String name,

        @NotNull(message = "Price cannot be empty or null.")
        @Positive(message = "Price must be greater than zero.")
        BigDecimal cost,

        @NotNull(message = "Duration cannot be empty or null.")
        BillingCycle duration,

        @NotNull(message = "Tenant's id cannot be empty or null.")
        @Positive(message = "Tenant's id must be greater than zero.")
        Long tenant_id
) {
        @Override
        public String toString() {
                return "{" + name +
                        ", cost=" + cost.toString() +
                        ", duration=" + duration.toString() +
                        ", tenantId=" + tenant_id + " }";
        }
}
