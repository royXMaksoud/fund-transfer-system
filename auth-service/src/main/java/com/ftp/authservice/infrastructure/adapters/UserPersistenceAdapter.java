package com.ftp.authservice.infrastructure.adapters;

import com.ftp.authservice.domain.model.User;
import com.ftp.authservice.domain.ports.out.LoadUserPort;
import com.ftp.authservice.domain.ports.out.SaveUserPort;
import com.ftp.authservice.infrastructure.db.entities.UserJpaEntity;
import com.ftp.authservice.infrastructure.repositories.UserJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserPersistenceAdapter implements LoadUserPort, SaveUserPort {

    private final UserJpaRepository userJpaRepository;

    public UserPersistenceAdapter(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public Optional<User> loadByUsername(String username) {
        // Get user from database by username
        return userJpaRepository.findByUsername(username)
                .map(this::mapToDomain); // Convert entity to domain model
    }

    private User mapToDomain(UserJpaEntity entity) {
        return new User(
                entity.getId(),
                entity.getUsername(),
                entity.getPassword(),
                entity.getEmail(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getRole()
        );
    }

    @Override
    public User save(User user) {
        // Convert domain model to JPA entity
        UserJpaEntity entity = new UserJpaEntity(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole()
        );
        // Save user in the database
        UserJpaEntity saved = userJpaRepository.save(entity);
        // Convert saved entity back to domain model
        return mapToDomain(saved);
    }

    @Override
    public boolean existsByUsername(String username) {
        // Check if username exists in the database
        return userJpaRepository.existsByUsername(username);
    }
}
