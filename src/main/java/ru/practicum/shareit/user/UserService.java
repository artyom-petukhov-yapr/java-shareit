package ru.practicum.shareit.user;

public interface UserService {
    UserDto getUser(Integer id);

    UserDto createUser(UserDto dto);

    UserDto patchUser(UserDto dto);

    void deleteUser(Integer id);
}
