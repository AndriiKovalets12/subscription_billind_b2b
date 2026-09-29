package dev.kovalets.subscription_billind_b2b.tenants.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateTenantNameDto(
        @NotBlank(message = "Name shouldn't be empty.")
        @Size(min = 2, max = 75, message = "Name length should be between 2 and 75 symbols.")
        String name
) {
    @Override
    public String toString() {
        return '{' +
                "name='" + name + '\'' +
                '}';
    }
}
