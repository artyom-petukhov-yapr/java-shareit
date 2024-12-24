package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ValidationException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User getUser(Integer id) {
        return userRepository.getReferenceById(id);
    }

    @Override
    public User createUser(User user) {
        validateUser(user, false);
        return userRepository.save(user);
    }

    @Override
    public User patchUser(User user) {
        validateUser(user, true);
        User repositoryUser = userRepository.getReferenceById(user.getId());
        if (user.getEmail() != null && !user.getEmail().isBlank()) {
            repositoryUser.setEmail(user.getEmail());
        }
        if (user.getName() != null && !user.getName().isBlank()) {
            repositoryUser.setName(user.getName());
        }
        return userRepository.save(repositoryUser);
    }

    @Override
    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }

    private void validateUser(User user, boolean allowEmptyValues) {
        if (!allowEmptyValues) {
            if (user == null) {
                throw new ValidationException("Информация о пользователе не может быть пустой");
            }
            if (user.getName() == null || user.getName().isBlank()) {
                throw new ValidationException(String.format("Имя пользователя (id=%d) не может быть пустым", user.getId()));
            }
            if (user.getEmail() == null || user.getEmail().isBlank()) {
                throw new ValidationException(String.format("Email пользователя (id=%d) не может быть пустым", user.getId()));
            }
        }

        // в случае если поле email не пустое, то валидация значения
        if (user.getEmail() != null && !user.getEmail().isBlank() && !user.getEmail().contains("@")) {
            throw new ValidationException(String.format("Email пользователя (id=%d) не удовлетворяет формату", user.getId()));
        }
    }
}
