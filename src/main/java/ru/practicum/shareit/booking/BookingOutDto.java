package ru.practicum.shareit.booking;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.user.UserDto;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingOutDto {
    Integer id;
    UserDto booker;
    ItemDto item;
    String start;
    String end;
    String status;
}
