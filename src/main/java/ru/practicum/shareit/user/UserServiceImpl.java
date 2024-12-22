package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ValidationException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto getUser(Integer id) {
        return userMapper.mapToUserDto(userRepository.get(id));
    }

    @Override
    public UserDto createUser(UserDto dto) {
        validateDto(dto, false);
        User user = userRepository.add(userMapper.mapToUser(dto));
        return userMapper.mapToUserDto(user);
    }

    @Override
    public UserDto patchUser(UserDto dto) {
        validateDto(dto, true);
        User user = userRepository.patch(userMapper.mapToUser(dto));
        return userMapper.mapToUserDto(user);
    }

    @Override
    public void deleteUser(Integer id) {
        userRepository.delete(id);
    }

    private void validateDto(UserDto dto, boolean allowEmptyValues) {
        if (!allowEmptyValues) {
            if (dto == null) {
                throw new ValidationException("Информация о пользователе не может быть пустой");
            }
            if (dto.getName() == null || dto.getName().isBlank()) {
                throw new ValidationException("Имя пользователя не может быть пустым");
            }
            if (dto.getEmail() == null || dto.getEmail().isBlank()) {
                throw new ValidationException("Email пользователя не может быть пустым");
            }
        }

        // в случае если поле email не пустое, то валидация значения
        if (dto.getEmail() != null && !dto.getEmail().isBlank() && !dto.getEmail().contains("@")) {
            throw new ValidationException("Email пользователя не удовлетворяет формату");
        }
    }
}
