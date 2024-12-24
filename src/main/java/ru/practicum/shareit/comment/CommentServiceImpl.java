package ru.practicum.shareit.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    @Override
    public Comment createComment(Comment comment) {
        Optional<User> author = userRepository.findById(comment.getAuthor().getId());
        if (author.isEmpty()) {
            throw new NotFoundException(String.format("Пользователь (id=%d) не зарегистрирован", comment.getAuthor().getId()));
        }
        comment.setAuthor(author.get());
        if (!itemRepository.existsById(comment.getItem().getId())) {
            throw new NotFoundException(String.format("Предмет (id=%d) не найден", comment.getItem().getId()));
        }

        if (!bookingRepository.canUserAddComment(comment.getAuthor().getId(), comment.getItem().getId())) {
            throw new ValidationException(String.format("Пользователь (id=%d) не может оставить комментарий к предмету (id=%d)",
                    comment.getAuthor().getId(), comment.getItem().getId()));
        }
        comment.setCreated(new Date());
        return commentRepository.save(comment);
    }

    @Override
    public List<Comment> findAllByItemId(Integer itemId) {
        return commentRepository.findAllByItemId(itemId);
    }
}
