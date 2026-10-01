package dev.kovalets.subscription_billind_b2b.subscriptions;

import dev.kovalets.subscription_billind_b2b.customers.CustomerEntity;
import dev.kovalets.subscription_billind_b2b.tenants.TenantEntity;
import dev.kovalets.subscription_billind_b2b.subscription_plans.SubscriptionPlanEntity;
import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "subscriptions")
public class SubscriptionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "subscription_gen_seq")
    @SequenceGenerator(name = "subscription_gen_seq", sequenceName = "subscription_seq")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_plan_id", nullable = false)
    private SubscriptionPlanEntity subscriptionPlan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private CustomerEntity customer;

    @Column(name = "start_date", nullable = false, updatable = false)
    private OffsetDateTime startDate;

    @Column(name = "next_billing_date", nullable = false)
    private OffsetDateTime nextBillingDate;

    @Column(name = "next_retry_date")
    private OffsetDateTime nextRetryDate;

    @Column(name = "status", nullable = false, length = 10)
    @Enumerated(value = EnumType.STRING)
    private SubscriptionStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", nullable = false)
    private TenantEntity tenant;

    @Column(name = "payment_attempt", nullable = false)
    private Integer paymentAttempt;

    protected SubscriptionEntity() {
    }

    public SubscriptionEntity(SubscriptionPlanEntity subscriptionPlan,
                              CustomerEntity customer,
                              OffsetDateTime nextBillingDate,
                              TenantEntity tenant) {
        this.subscriptionPlan = subscriptionPlan;
        this.customer = customer;
        this.startDate = OffsetDateTime.now();
        this.nextBillingDate = nextBillingDate;
        this.status = SubscriptionStatus.ACTIVE;
        this.tenant = tenant;
        this.paymentAttempt = 0;
        this.nextRetryDate = null;
    }

    public Long getId() {
        return id;
    }

    public SubscriptionPlanEntity getSubscriptionPlan() {
        return subscriptionPlan;
    }

    public CustomerEntity getCustomer() {
        return customer;
    }

    public OffsetDateTime getStartDate() {
        return startDate;
    }

    public OffsetDateTime getNextBillingDate() {
        return nextBillingDate;
    }

    public SubscriptionStatus getStatus() {
        return status;
    }

    public TenantEntity getTenant() {
        return tenant;
    }

    public Integer getPaymentAttempt() {
        return paymentAttempt;
    }

    public void cancel() {
        this.status = SubscriptionStatus.CANCELED;
    }

    public void activate() {
        this.status = SubscriptionStatus.ACTIVE;
    }

    public void markAsPastDue() {
        this.status = SubscriptionStatus.PAST_DUE;
    }

    public boolean incrementPaymentAttempt(){
        if (this.paymentAttempt != 4){
            this.paymentAttempt++;
            return true;
        }
        return false;
    }

    public void resetPaymentAttempt(){
        this.paymentAttempt = 0;
    }

    public void updateNextRetryDate(OffsetDateTime currentPeriodStart) {
        switch (this.paymentAttempt){
            case 1:
                this.nextRetryDate = currentPeriodStart.plusDays(1);
                break;
            case 2:
                this.nextRetryDate = currentPeriodStart.plusDays(2);
                break;
            case 3:
                this.nextRetryDate = currentPeriodStart.plusDays(5);
                break;
        }
    }

    public void setNextBillingDate(OffsetDateTime nextBillingDate) {
        this.nextBillingDate = nextBillingDate;
    }
}
