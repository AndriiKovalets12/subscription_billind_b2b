package dev.kovalets.subscription_billind_b2b.tenants;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "tenants")
public class TenantEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 75)
    private String name;

    public TenantEntity(String name) {
        this.name = name;
    }

    protected TenantEntity() {}

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void updateName(String newName){
        this.name = newName;
    }
}
