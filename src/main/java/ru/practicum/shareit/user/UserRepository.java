package ru.practicum.shareit.user;

public interface UserRepository {
    User get(Integer userId);

    User add(User user);

    User patch(User user);

    void delete(Integer userId);
}
