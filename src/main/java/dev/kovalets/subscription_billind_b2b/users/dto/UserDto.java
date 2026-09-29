package dev.kovalets.subscription_billind_b2b.users.dto;

public record UserDto(
        Long id,
        String firstName,
        String lastName,
        String email,
        String userRole,
        Long tenantId
) {
}
