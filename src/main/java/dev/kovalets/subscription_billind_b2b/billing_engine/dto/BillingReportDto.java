package dev.kovalets.subscription_billind_b2b.billing_engine.dto;

import java.util.List;

public record BillingReportDto(
        Long totalProcessed,
        Long totalSuccessful,
        Long failedPayments,
        List<BillingResult> details
) {
}
