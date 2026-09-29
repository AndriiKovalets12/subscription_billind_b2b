package dev.kovalets.subscription_billind_b2b.customers.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SyncCustomerDto(
        @NotBlank(message = "First name cannot be empty.")
        @Size(min = 1, max = 50, message = "First name shouldn't be longer than two symbols and shorter than fifty symbols.")
        String firstName,

        @NotBlank(message = "Last name cannot be empty.")
        @Size(min = 1, max = 50, message = "Last name shouldn't be longer than two symbols and shorter than fifty symbols.")
        String lastName,

        @Email(message = "Email must be in correct format.")
        @Size(max = 75, message = "Email shouldn't be longer than seventy five symbols.")
        String email
) {
    @Override
    public String toString() {
        return '{' +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
