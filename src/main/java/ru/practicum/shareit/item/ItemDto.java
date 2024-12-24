package ru.practicum.shareit.item;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.BookingOutDto;
import ru.practicum.shareit.comment.CommentDto;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemDto {
    Integer id;
    Integer ownerId;
    String name;
    String description;
    Boolean available;
    BookingOutDto lastBooking;
    BookingOutDto nextBooking;
    List<CommentDto> comments;
}
