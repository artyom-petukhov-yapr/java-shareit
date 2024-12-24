package ru.practicum.shareit.booking;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingInDto {
    Integer id;
    Integer bookerId;
    Integer itemId;
    String start;
    String end;
    String status;
}
