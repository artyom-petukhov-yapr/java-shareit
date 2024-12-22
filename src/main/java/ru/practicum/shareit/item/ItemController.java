package ru.practicum.shareit.item;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    /**
     * Добавить информацию о предмете
     */
    @PostMapping
    public ItemDto createItem(@RequestHeader(USER_ID_HEADER) Integer userId, @RequestBody ItemDto item) {
        item.setOwnerId(userId);
        return itemService.createItem(item);
    }

    /**
     * Обновить информацию о предмете
     */
    @PatchMapping("/{id}")
    public ItemDto patchItem(@PathVariable Integer id, @RequestHeader(USER_ID_HEADER) Integer userId, @RequestBody ItemDto item) {
        item.setId(id);
        item.setOwnerId(userId);
        return itemService.patchItem(item);
    }

    /**
     * Поиск предметов по тексту
     * /items/search?text={text}
     */
    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam String text) {
        return itemService.findByText(text);
    }

    /**
     * Получить информацию о предмете
     */
    @GetMapping("/{id}")
    public ItemDto getItem(@PathVariable Integer id) {
        return itemService.getItem(id);
    }

    /**
     * Удалить предмет
     */
    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable Integer id) {
        itemService.deleteItem(id);
    }

    /**
     * Получить список предметов пользователя
     */
    @GetMapping
    public List<ItemDto> getUserItems(@RequestHeader(USER_ID_HEADER) Integer userId) {
        return itemService.getUserItems(userId);
    }
}
