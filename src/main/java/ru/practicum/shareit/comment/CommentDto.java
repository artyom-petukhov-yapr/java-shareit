package ru.practicum.shareit.comment;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentDto {
    Integer id;
    Integer authorId;
    String authorName;
    Integer itemId;
    String text;
    String created;
}
