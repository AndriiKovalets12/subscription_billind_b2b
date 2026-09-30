package dev.kovalets.subscription_billind_b2b.invoices.dto;

import dev.kovalets.subscription_billind_b2b.invoices.InvoiceStatus;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record CreateInvoiceDto(
        @NotNull(message = "Subscription's id cannot be empty or null.")
        @Positive(message = "Subscription's id must be greater than zero.")
        Long subscriptionId,

        @NotNull(message = "Amount cannot be empty or null.")
        @Positive(message = "Amount must be greater than zero.")
        BigDecimal amount,

        @NotNull(message = "Status cannot be empty or null.")
        InvoiceStatus status,

        OffsetDateTime billingPeriodStart,

        @Future
        OffsetDateTime billingPeriodEnd,

        @NotBlank
        String idempotencyKey,

        @NotNull(message = "Tenant's id cannot be empty or null.")
        @Positive(message = "Tenant's id must be greater than zero.")
        Long tenantId
) {
    @Override
    public String toString() {
        return "{" +
                "subscriptionId=" + subscriptionId +
                ", amount=" + amount +
                ", status=" + status +
                ", billingPeriodStart=" + billingPeriodStart +
                ", billingPeriodEnd=" + billingPeriodEnd +
                ", idempotencyKey='" + idempotencyKey + '\'' +
                ", tenantId=" + tenantId +
                '}';
    }

    public void updateStatus(InvoiceStatus invoiceStatus) {
    }
}
