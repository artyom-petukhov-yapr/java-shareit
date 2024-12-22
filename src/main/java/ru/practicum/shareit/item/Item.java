package ru.practicum.shareit.item;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Item {
    Integer id;
    Integer ownerId;
    String name;
    String description;
    Boolean available;
}
