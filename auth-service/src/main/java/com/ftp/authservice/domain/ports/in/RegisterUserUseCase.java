package com.ftp.authservice.domain.ports.in;

import com.ftp.authservice.domain.model.User;

public interface RegisterUserUseCase {
    User register(User user);
}
