package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.RepositoryException;
import ru.practicum.shareit.user.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
@RequiredArgsConstructor
public class InMemoryItemRepository implements ItemRepository {
    private final UserRepository userRepository;
    private final Map<Integer, Item> items = new HashMap<>();
    private final AtomicInteger idCounter = new AtomicInteger(0);

    @Override
    public Item get(Integer id) {
        Item item = items.get(id);
        if (item == null) {
            throw new RepositoryException(String.format("Предмет с id = %d не найден", id));
        }
        return item;
    }

    @Override
    public Item add(Item item) {
        item.setId(idCounter.incrementAndGet());

        itemNameNotNullConstraint(item);
        itemDescriptionNotNullConstraint(item);
        itemOwnerIdNotNullConstraint(item);
        itemOwnerIdForeignKeyConstraint(item);
        itemAvailableNotNullConstraint(item);

        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item patch(Item item) {
        Item result = items.get(item.getId());

        itemOwnerIdNotNullConstraint(item);
        itemOwnerIdForeignKeyConstraint(item);

        if (item.getName() != null && !item.getName().isEmpty()) {
            result.setName(item.getName());
        }
        if (item.getDescription() != null && !item.getDescription().isEmpty()) {
            result.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            result.setAvailable(item.getAvailable());
        }
        return item;
    }

    @Override
    public void delete(Integer id) {
        items.remove(id);
    }

    @Override
    public List<Item> getUserItems(Integer userId) {
        return items.values().stream().filter(item -> Objects.equals(item.getOwnerId(), userId)).toList();
    }

    @Override
    public List<Item> findByText(String text) {
        if (text == null || text.isEmpty()) {
            return List.of();
        }
        String lowerCaseText = text.toLowerCase();
        return items.values().stream()
                .filter(item -> item.getAvailable()
                        && (item.getName().toLowerCase().contains(lowerCaseText)
                        || item.getDescription().toLowerCase().contains(lowerCaseText))
                )
                .toList();
    }

    void itemNameNotNullConstraint(Item item) {
        if (item.getName() == null || item.getName().isEmpty()) {
            throw new RepositoryException("Поле name не может быть пустым");
        }
    }

    void itemDescriptionNotNullConstraint(Item item) {
        if (item.getDescription() == null || item.getDescription().isEmpty()) {
            throw new RepositoryException("Поле description не может быть пустым");
        }
    }

    void itemOwnerIdNotNullConstraint(Item item) {
        if (item.getOwnerId() == null) {
            throw new RepositoryException("Поле ownerId не может быть пустым");
        }
    }

    void itemOwnerIdForeignKeyConstraint(Item item) {
        try {
            userRepository.get(item.getOwnerId());
        } catch (RepositoryException e) {
            // только в этом случае ожидается 404 ошибка
            throw new NotFoundException("Пользователя с id = " + item.getOwnerId() + " не существует");
        }
    }

    void itemAvailableNotNullConstraint(Item item) {
        if (item.getAvailable() == null) {
            throw new RepositoryException("Поле available не может быть пустым");
        }
    }
}
