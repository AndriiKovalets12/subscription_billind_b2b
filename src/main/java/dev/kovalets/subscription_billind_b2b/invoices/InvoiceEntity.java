package dev.kovalets.subscription_billind_b2b.invoices;

import dev.kovalets.subscription_billind_b2b.subscriptions.SubscriptionEntity;
import dev.kovalets.subscription_billind_b2b.tenants.TenantEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.CurrentTimestamp;
import org.hibernate.generator.EventType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Entity
@Table(name = "invoices")
public class InvoiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_id", nullable = false, updatable = false)
    private SubscriptionEntity subscription;

    @Column(name = "amount", nullable = false, updatable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    @Column(name = "status", nullable = false, length = 7)
    @Enumerated(EnumType.STRING)
    private InvoiceStatus status;

    @CurrentTimestamp(event = EventType.INSERT)
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "billing_period_start", nullable = false)
    private OffsetDateTime billingPeriodStart;

    @Column(name = "billing_period_end", nullable = false)
    private OffsetDateTime billingPeriodEnd;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    @Column(name = "idempotency_key", nullable = false, unique = true)
    private String idempotencyKey;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenantId", nullable = false)
    private TenantEntity tenant;

    protected InvoiceEntity() {
    }

    public InvoiceEntity(SubscriptionEntity subscription,
                         BigDecimal amount,
                         InvoiceStatus status,
                         OffsetDateTime billingPeriodStart,
                         OffsetDateTime billingPeriodEnd,
                         Long version,
                         String idempotencyKey,
                         TenantEntity tenant) {
        this.subscription = subscription;
        this.amount = amount;
        this.status = status;
        this.createdAt = OffsetDateTime.now();
        this.billingPeriodStart = billingPeriodStart;
        this.billingPeriodEnd = billingPeriodEnd;
        this.version = version;
        this.idempotencyKey = idempotencyKey;
        this.tenant = tenant;
    }

    public Long getId() {
        return id;
    }

    public SubscriptionEntity getSubscription() {
        return subscription;
    }

    public InvoiceStatus getStatus() {
        return status;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getBillingPeriodStart() {
        return billingPeriodStart;
    }

    public OffsetDateTime getBillingPeriodEnd() {
        return billingPeriodEnd;
    }

    public Long getVersion() {
        return version;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getIdempotencyKey() {
        return idempotencyKey;
    }

    public TenantEntity getTenant() {
        return tenant;
    }

    public void setStatus(InvoiceStatus status){
        this.status = status;
    }
}
