package ru.practicum.shareit.item;

import java.util.List;

public interface ItemService {
    ItemDto getItem(Integer id);

    ItemDto createItem(ItemDto dto);

    ItemDto patchItem(ItemDto dto);

    void deleteItem(Integer id);

    List<ItemDto> getUserItems(Integer userId);

    List<ItemDto> findByText(String text);
}
