package ru.practicum.shareit.user;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserMapper {
    public UserDto mapToUserDto(User user) {
        UserDto result = new UserDto();
        result.setId(user.getId());
        result.setName(user.getName());
        result.setEmail(user.getEmail());
        return result;
    }

    public User mapToUser(UserDto user) {
        User result = new User();
        result.setId(user.getId());
        result.setName(user.getName());
        result.setEmail(user.getEmail());
        return result;
    }
}