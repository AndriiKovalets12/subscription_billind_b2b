package dev.kovalets.subscription_billind_b2b.users;

import dev.kovalets.subscription_billind_b2b.customers.dto.CreateCustomerDto;
import dev.kovalets.subscription_billind_b2b.users.dto.CreateUserDto;
import dev.kovalets.subscription_billind_b2b.users.dto.UpdateProfileUserDto;
import dev.kovalets.subscription_billind_b2b.users.dto.UserDto;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers(){

        log.info("Called getAllUsers().");

        List<UserDto> users = service.getAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id){

        log.info("Called getUserById(Long id) with id={}.", id);

        UserDto user = service.getById(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<UserDto> createNewUser(@Valid @RequestBody CreateUserDto userToCreate){

        log.info("Called createNewUser(CreateUserDto userToCreate) with userToCreate:{}.", userToCreate);

        UserDto createdUser = service.create(userToCreate);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PatchMapping("/{id}/profile")
    public ResponseEntity<UserDto> updateUserProfile(@PathVariable Long id,
                                                     @Valid @RequestBody UpdateProfileUserDto profileToUpdate){

        log.info("Called updateUserProfile(Long id, UpdateProfileUserDto profileToUpdate) with id={}, profileToUpdate:{}", id, profileToUpdate);

        UserDto updatedProfile = service.updateProfile(id, profileToUpdate);
        return ResponseEntity.ok(updatedProfile);
    }

    ///Дописать updateEmail(String newEmail) з генерацією листа для підтвердження
    ///Дописть updatePassword(String newPassword)

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivateUser(@PathVariable Long id){

        log.info("Called deactivateUser(Long id) with id={}", id);

        service.deactivate(id);
        return ResponseEntity.noContent().build();
    }


}
