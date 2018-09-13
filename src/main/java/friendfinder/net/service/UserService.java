package friendfinder.net.service;

import friendfinder.net.model.User;

import java.util.Optional;

public interface UserService {

    boolean existsByEmail(String email);

    void add(User user);

    Optional<User> getByEmail(String email);

    void update(User user);
}