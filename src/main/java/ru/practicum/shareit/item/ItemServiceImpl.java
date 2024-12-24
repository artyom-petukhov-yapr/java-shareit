package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;

    @Override
    public Item getItem(Integer id) {
        return itemRepository.getReferenceById(id);
    }

    @Override
    public Item createItem(Item item) {
        validateItem(item);
        try {
            return itemRepository.save(item);
        } catch (DataIntegrityViolationException e) {
            // postman-тесты ожидают 404 ошибку в случае, если владелец не найден
            throw new NotFoundException(String.format("Владелец предмета (owner_id=%d) не найден", item.getOwner().getId()));
        }
    }

    @Override
    public Item patchItem(Item item) {
        Item repositoryItem = itemRepository.getItemByIdAndOwnerId(item.getId(), item.getOwner().getId());
        if (repositoryItem == null) {
            throw new NotFoundException(String.format("Предмет (id=%d) для владельца (id=%d) не найден", item.getId(), item.getOwner().getId()));
        }
        if (item.getName() != null && !item.getName().isBlank()) {
            repositoryItem.setName(item.getName());
        }
        if (item.getDescription() != null && !item.getDescription().isBlank()) {
            repositoryItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            repositoryItem.setAvailable(item.getAvailable());
        }
        try {
            return itemRepository.save(repositoryItem);
        } catch (DataIntegrityViolationException e) {
            // postman-тесты ожидают 404 ошибку в случае, если владелец не найден
            throw new NotFoundException(String.format("Владелец предмета (owner_id=%d) не найден", item.getOwner().getId()));
        }
    }

    @Override
    public void deleteItem(Integer id) {
        itemRepository.deleteById(id);
    }

    @Override
    public List<Item> getUserItems(Integer userId) {
        return itemRepository.findItemsByOwnerId(userId);
    }

    @Override
    public List<Item> findByText(String text) {
        return itemRepository.findByText(text);
    }

    private void validateItem(Item item) {
        if (item == null) {
            throw new ValidationException("Информация о предмете не может быть пустой");
        }
        if (item.getName() == null || item.getName().isBlank()) {
            throw new ValidationException("Название предмета не может быть пустым");
        }
        if (item.getOwner() == null) {
            throw new ValidationException("Владелец предмета не может быть пустым");
        }
        if (item.getDescription() == null || item.getDescription().isBlank()) {
            throw new ValidationException("Описание предмета не может быть пустым");
        }
        if (item.getAvailable() == null) {
            throw new ValidationException("Доступность предмета не может быть пустой");
        }
    }
}
