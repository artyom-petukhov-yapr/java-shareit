package ru.practicum.shareit.comment;

import java.util.List;

public interface CommentService {
    Comment createComment(Comment comment);

    List<Comment> findAllByItemId(Integer itemId);
}
