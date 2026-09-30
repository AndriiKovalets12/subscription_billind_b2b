package dev.kovalets.subscription_billind_b2b.billing_engine.dto;

import dev.kovalets.subscription_billind_b2b.invoices.InvoiceStatus;

public record BillingResult(
        Long subscriptionId,
        Long invoiceId,
        InvoiceStatus status,
        String message
) {
}
