package dev.kovalets.subscription_billind_b2b.users;

import dev.kovalets.subscription_billind_b2b.tenants.TenantEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_seq_gen")
    @SequenceGenerator(name = "users_seq_gen", sequenceName = "users_seq")
    private Long id;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(name = "email", nullable = false, unique = true, length = 75)
    private String email;

    @Column(name = "hash_password", nullable = false)
    private String hashPassword;

    @Column(name = "user_role", nullable = false, length = 14)
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", nullable = false)
    private TenantEntity tenant;

    @Column(name = "is_active", nullable = false)
    boolean isActive;

    protected UserEntity() {
    }

    public UserEntity(String firstName, String lastName, String email, String hashPassword, UserRole userRole, TenantEntity tenant) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.hashPassword = hashPassword;
        this.tenant = tenant;
        this.userRole = userRole;
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

    public String getHashPassword() {
        return hashPassword;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public TenantEntity getTenant() {
        return tenant;
    }

    public boolean isActive() {
        return isActive;
    }

    public void updateProfile(String firstName, String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public void deactivate() {
        this.isActive = false;
    }
}
