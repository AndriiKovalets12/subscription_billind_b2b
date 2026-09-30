package dev.kovalets.subscription_billind_b2b.subscription_plans;

import dev.kovalets.subscription_billind_b2b.tenants.TenantEntity;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "subscription_plans")
public class SubscriptionPlanEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "name", nullable = false, length = 25)
    private String name;

    @Column(name = "cost", nullable = false, precision = 19, scale = 4)
    private BigDecimal cost;

    @Column(name = "duration", nullable = false)
    @Enumerated(EnumType.STRING)
    private BillingCycle duration;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenantId", nullable = false)
    private TenantEntity tenant;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;


    public SubscriptionPlanEntity(String name,
                                  BigDecimal cost,
                                  BillingCycle duration,
                                  TenantEntity tenant,
                                  boolean isActive) {
        this.name = name;
        this.cost = cost;
        this.duration = duration;
        this.tenant = tenant;
        this.isActive = isActive;
    }

    protected SubscriptionPlanEntity() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public TenantEntity getTenant() {
        return tenant;
    }

    public BillingCycle getDuration() {
        return duration;
    }

    public boolean isActive() {
        return isActive;
    }

    public void archive(){
        isActive = false;
    }

    public void updateName(String newName){
        if (!(newName == null || newName.isEmpty())) {
            this.name = newName;
        }
    }

}

