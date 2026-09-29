package dev.kovalets.subscription_billind_b2b.tenants.dto;

public record TenantDto(
        Long id,
        String name
) {
    @Override
    public String toString() {
        return  '{' +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
