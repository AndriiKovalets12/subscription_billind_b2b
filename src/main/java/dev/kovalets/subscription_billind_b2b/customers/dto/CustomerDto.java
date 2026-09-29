package dev.kovalets.subscription_billind_b2b.customers.dto;

import jakarta.validation.constraints.NotNull;

public record CustomerDto(
        Long id,
        String firstName,
        String lastName,
        String email,
        String customerExternalId,
        Long tenantId
) {
    @Override
    public String toString() {
        return '{' +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", customerExternalId='" + customerExternalId + '\'' +
                ", tenantId=" + tenantId +
                '}';
    }
}
