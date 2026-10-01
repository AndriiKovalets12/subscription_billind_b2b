package dev.kovalets.subscription_billind_b2b.invoices;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvoiceRepository extends JpaRepository<InvoiceEntity, Long> {
    boolean existsByIdempotencyKey(String idempotencyKey);

    InvoiceEntity findByIdempotencyKey(String idempotencyKey);
}
