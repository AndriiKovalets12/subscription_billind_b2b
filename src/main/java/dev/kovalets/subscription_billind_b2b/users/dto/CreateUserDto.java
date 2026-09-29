package dev.kovalets.subscription_billind_b2b.users.dto;

import dev.kovalets.subscription_billind_b2b.users.UserRole;
import jakarta.validation.constraints.*;

public record CreateUserDto(
        @NotBlank(message = "First name cannot be empty.")
        @Size(min = 1, max = 50, message = "First name shouldn't be longer than two symbols and shorter than fifty symbols.")
        String firstName,

        @NotBlank(message = "Last name cannot be empty.")
        @Size(min = 1, max = 50, message = "Last name shouldn't be longer than two symbols and shorter than fifty symbols.")
        String lastName,

        @Email(message = "Email must be in correct format.")
        @Size(max = 75, message = "Email shouldn't be longer than seventy five symbols.")
        String email,

        @NotBlank(message = "Password cannot be empty.")
        String rawPassword,

        @NotNull(message = "User role cannot be empty.")
        UserRole userRole,

        @NotNull(message = "Tenant's id cannot be empty or null.")
        @Positive(message = "Tenant's id must be greater than zero.")
        Long tenantId
) {
    @Override
    public String toString() {
        return "{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", rawPassword='" + rawPassword + '\'' +
                ", userRole='" + userRole + '\'' +
                ", tenantId=" + tenantId +
                '}';
    }
}
