package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.RepositoryException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private final Map<Integer, User> users = new HashMap<>();
    private final AtomicInteger idCounter = new AtomicInteger(0);

    @Override
    public User get(Integer userId) {
        User result = users.get(userId);
        if (result == null) {
            throw new RepositoryException(String.format("Пользователь с id = %d не найден", userId));
        }
        return result;
    }

    @Override
    public User add(User user) {
        user.setId(idCounter.incrementAndGet());

        userNameNotNullConstraint(user);
        userNameUniqueConstraint(user);
        userEmailNotNullConstraint(user);
        userEmailUniqueConstraint(user);

        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User patch(User user) {
        User result = get(user.getId());
        if (user.getName() != null && !user.getName().isEmpty()) {
            userNameUniqueConstraint(user);
            result.setName(user.getName());
        }
        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            userEmailUniqueConstraint(user);
            result.setEmail(user.getEmail());
        }
        return user;
    }

    @Override
    public void delete(Integer userId) {
        users.remove(userId);
    }

    private void userNameNotNullConstraint(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            throw new RepositoryException("Поле name не может быть пустым");
        }
    }

    private void userNameUniqueConstraint(User user) {
        if (users.values().stream().anyMatch(u -> !Objects.equals(u.getId(), user.getId()) && u.getName().equals(user.getName()))) {
            throw new RepositoryException(String.format("Пользователь с name = %s уже зарегистрирован", user.getName()));
        }
    }

    private void userEmailNotNullConstraint(User user) {
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new RepositoryException("Поле email не может быть пустым");
        }
    }

    private void userEmailUniqueConstraint(User user) {
        if (users.values().stream().anyMatch(u -> !Objects.equals(u.getId(), user.getId()) && u.getEmail().equals(user.getEmail()))) {
            throw new RepositoryException(String.format("Пользователь с email = %s уже зарегистрирован", user.getEmail()));
        }
    }
}
