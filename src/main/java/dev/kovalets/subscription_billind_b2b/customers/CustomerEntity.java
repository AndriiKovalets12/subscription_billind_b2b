package dev.kovalets.subscription_billind_b2b.customers;

import dev.kovalets.subscription_billind_b2b.tenants.TenantEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "customers",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"external_customer_id", "tenant_id"})})
public class CustomerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(name = "email", nullable = false, length = 75)
    private String email;

    @Column(name = "external_customer_id", nullable = false, updatable = false)
    private String externalCustomerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenantId", nullable = false)
    private TenantEntity tenant;

    @Column(name = "isActive", nullable = false)
    private boolean isActive;


    protected CustomerEntity() {
    }

    public CustomerEntity(String firstName,
                          String lastName,
                          String email,
                          String externalCustomerId,
                          TenantEntity tenant) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.externalCustomerId = externalCustomerId;
        this.tenant = tenant;
        this.isActive = true;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getExternalCustomerId() {
        return externalCustomerId;
    }

    public TenantEntity getTenant() {
        return tenant;
    }

    public boolean isActive() {
        return isActive;
    }

    public void updateProfile(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public void deactivate(){
        this.isActive = false;
    }

    public void activate() {
        this.isActive = true;
    }
}
