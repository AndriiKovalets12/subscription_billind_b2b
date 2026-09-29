package dev.kovalets.subscription_billind_b2b.invoices.dto;

import dev.kovalets.subscription_billind_b2b.invoices.InvoiceStatus;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record InvoiceDto(
        Long subscriptionId,
        BigDecimal amount,
        InvoiceStatus status,
        OffsetDateTime createdAt,
        OffsetDateTime billingPeriodStart,
        OffsetDateTime billingPeriodEnd,
        Long tenantId
) {
    @Override
    public String toString() {
        return "{" +
                "subscriptionId=" + subscriptionId +
                ", amount=" + amount +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", billingPeriodStart=" + billingPeriodStart +
                ", billingPeriodEnd=" + billingPeriodEnd +
                ", tenantId=" + tenantId +
                '}';
    }
}
