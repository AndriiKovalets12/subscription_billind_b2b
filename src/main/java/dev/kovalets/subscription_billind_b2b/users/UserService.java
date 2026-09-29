package dev.kovalets.subscription_billind_b2b.users;

import dev.kovalets.subscription_billind_b2b.tenants.TenantEntity;
import dev.kovalets.subscription_billind_b2b.tenants.TenantRepository;
import dev.kovalets.subscription_billind_b2b.users.dto.CreateUserDto;
import dev.kovalets.subscription_billind_b2b.users.dto.UpdateProfileUserDto;
import dev.kovalets.subscription_billind_b2b.users.dto.UserDto;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final TenantRepository tenantRepository;

    public UserService(UserRepository userRepository, TenantRepository tenantRepository) {
        this.userRepository = userRepository;
        this.tenantRepository = tenantRepository;
    }

    public List<UserDto> getAll(){
        List<UserEntity> userEntities = userRepository.findAllActive();
        return mapperToDto(userEntities);
    }

    public UserDto getById(Long id) {
        UserEntity user = userRepository
                .findActiveById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id=" + id + " not found."));

        return mapperToDto(user);
    }

    @Transactional
    public UserDto create(@Valid CreateUserDto userToCreate) {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        TenantEntity tenant = tenantRepository
                .findById(userToCreate.tenantId())
                .orElseThrow(EntityNotFoundException::new);

        if (!userRepository.existsByEmailAndIsActiveTrue(userToCreate.email())){
            UserEntity createdUser =
                    new UserEntity(
                            userToCreate.firstName(),
                            userToCreate.lastName(),
                            userToCreate.email(),
                            encoder.encode(userToCreate.rawPassword()),
                            userToCreate.userRole(),
                            tenant
                    );
            userRepository.save(createdUser);
            return mapperToDto(createdUser);

        } else {
            throw new EntityExistsException("Entity with this email already exists");
        }

    }

    @Transactional
    public UserDto updateProfile(Long id, @Valid UpdateProfileUserDto profileToUpdate) {
        UserEntity user = userRepository
                .findActiveById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id=" + id + " not found."));

        user.updateProfile(
                profileToUpdate.firstName(),
                profileToUpdate.lastName()
        );

        return mapperToDto(user);
    }

    @Transactional
    public void deactivate(Long id) {
        UserEntity user = userRepository
                .findActiveById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id=" + id + " not found."));

        user.deactivate();
    }

    private UserDto mapperToDto(UserEntity entity){
        return new UserDto(
                entity.getId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getEmail(),
                entity.getUserRole().toString(),
                entity.getTenant().getId());
    }

    private List<UserDto> mapperToDto(List<UserEntity> entities){
        return entities.stream().map(this::mapperToDto).toList();
    }
}
