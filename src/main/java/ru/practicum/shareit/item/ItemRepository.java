package ru.practicum.shareit.item;

import java.util.List;

public interface ItemRepository {
    Item get(Integer id);

    Item add(Item item);

    Item patch(Item item);

    void delete(Integer id);

    List<Item> getUserItems(Integer userId);

    List<Item> findByText(String text);
}
