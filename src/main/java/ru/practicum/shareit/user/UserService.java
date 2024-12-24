package ru.practicum.shareit.user;

public interface UserService {
    User getUser(Integer id);

    User createUser(User user);

    User patchUser(User user);

    void deleteUser(Integer id);
}
