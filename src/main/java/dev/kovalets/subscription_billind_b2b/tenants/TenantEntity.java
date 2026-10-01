package dev.kovalets.subscription_billind_b2b.tenants;

import jakarta.persistence.*;

@Entity
@Table(name = "tenants")
public class TenantEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tenants_seq_gen")
    @SequenceGenerator(name = "tenants_seq_gen", sequenceName = "tenants_seq")
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
