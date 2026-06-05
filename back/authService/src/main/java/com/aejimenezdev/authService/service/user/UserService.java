package com.aejimenezdev.authService.service.user;

import com.aejimenezdev.authService.model.User;
import java.util.Optional;

public interface UserService {
    User getByEmail(String email);
    boolean existsByEmail(String email);
    User save(User user);
}