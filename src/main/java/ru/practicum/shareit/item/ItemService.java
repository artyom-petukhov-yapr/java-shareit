package ru.practicum.shareit.item;

import java.util.List;

public interface ItemService {
    Item getItem(Integer id);

    Item createItem(Item item);

    Item patchItem(Item item);

    void deleteItem(Integer id);

    List<Item> getUserItems(Integer userId);

    List<Item> findByText(String text);
}
