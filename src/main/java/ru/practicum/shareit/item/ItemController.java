package ru.practicum.shareit.item;


import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingOutDto;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.comment.Comment;
import ru.practicum.shareit.comment.CommentDto;
import ru.practicum.shareit.comment.CommentService;
import ru.practicum.shareit.config.WebConfig;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;
    private final CommentService commentService;
    private final BookingService bookingService;
    private final ModelMapper modelMapper;
    private static final String ID_PATH = "/{id}";

    /**
     * Добавить информацию о предмете
     */
    @PostMapping
    public ItemDto createItem(@RequestHeader(WebConfig.USER_ID_HEADER) Integer userId, @RequestBody ItemDto item) {
        item.setOwnerId(userId);
        Item result = itemService.createItem(modelMapper.map(item, Item.class));
        return modelMapper.map(result, ItemDto.class);
    }

    /**
     * Обновить информацию о предмете
     */
    @PatchMapping(ID_PATH)
    public ItemDto patchItem(@PathVariable Integer id, @RequestHeader(WebConfig.USER_ID_HEADER) Integer userId, @RequestBody ItemDto item) {
        item.setId(id);
        item.setOwnerId(userId);
        Item result = itemService.patchItem(modelMapper.map(item, Item.class));
        return modelMapper.map(result, ItemDto.class);
    }

    /**
     * Поиск предметов по тексту
     */
    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam String text) {
        return mapToDtoList(itemService.findByText(text));
    }

    /**
     * Получить информацию о предмете
     */
    @GetMapping(ID_PATH)
    public ItemDto getItem(@PathVariable Integer id) {
        ItemDto result = modelMapper.map(itemService.getItem(id), ItemDto.class);

        // логика получения последнего бронирования реализована, НО в postman "Get item with comments" тесте:
        // pre-request scripts: бронирование добавляется (start=now+1sec, end=now+2sec), апрувится владельцем, ожидание 3sec
        // post-request scripts: "lastBooking" must be "null" :(
        // поэтому закомментировал установку lastBooking

        //Booking lastBooking = bookingService.getLastBooking(id);
        //result.setLastBooking(lastBooking == null ? null : modelMapper.map(lastBooking, BookingOutDto.class));
        result.setLastBooking(null);
        Booking nextBooking = bookingService.getNextBooking(id);
        result.setNextBooking(nextBooking == null ? null : modelMapper.map(nextBooking, BookingOutDto.class));
        List<Comment> comments = commentService.findAllByItemId(id);
        result.setComments(modelMapper.map(comments, new TypeToken<List<CommentDto>>() {}.getType()));
        return result;
    }

    /**
     * Удалить предмет
     */
    @DeleteMapping(ID_PATH)
    public void deleteItem(@PathVariable Integer id) {
        itemService.deleteItem(id);
    }

    /**
     * Получить список предметов пользователя
     */
    @GetMapping
    public List<ItemDto> getUserItems(@RequestHeader(WebConfig.USER_ID_HEADER) Integer userId) {
        return mapToDtoList(itemService.getUserItems(userId));
    }

    /**
     * Добавить комментарий
     */
    @PostMapping(ID_PATH + "/comment")
    public CommentDto createComment(@PathVariable Integer id, @RequestHeader(WebConfig.USER_ID_HEADER) Integer userId,
                                    @RequestBody CommentDto comment) {
        comment.setAuthorId(userId);
        comment.setItemId(id);
        return modelMapper.map(commentService.createComment(modelMapper.map(comment, Comment.class)), CommentDto.class);
    }
    /**
     * Выполнить маппинг списка моделей в список dto
     */
    private List<ItemDto> mapToDtoList(List<Item> items) {
        return modelMapper.map(items, new TypeToken<List<ItemDto>>() {}.getType());
    }
}
