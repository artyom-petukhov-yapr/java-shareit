package ru.practicum.shareit.booking;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public Booking createBooking(Booking booking) {
        // Note: проверки реализованы в порядке ожидаемом тестами postman
        Optional<User> foundBooker = userRepository.findById(booking.getBooker().getId());
        if (foundBooker.isEmpty()) {
            throw new NotFoundException(String.format("Пользователь (id=%d) не зарегистрирован", booking.getBooker().getId()));
        }
        booking.setBooker(foundBooker.get());

        Optional<Item> foundItem = itemRepository.findById(booking.getItem().getId());
        if (foundItem.isEmpty()) {
            throw new NotFoundException(String.format("Предмет (id=%d) не найден", booking.getItem().getId()));
        }
        booking.getItem().setName(foundItem.get().getName());
        if (booking.getStart() == null) {
            throw new ValidationException("Дата начала бронирования не может быть пустой");
        }
        if (booking.getEnd() == null) {
            throw new ValidationException("Дата окончания бронирования не может быть пустой");
        }
        if (booking.getStart().after(booking.getEnd())) {
            throw new ValidationException("Дата окончания бронирования не может быть раньше даты начала");
        }
        if (booking.getStart().equals(booking.getEnd())) {
            throw new ValidationException("Дата окончания бронирования не может быть равна дате начала");
        }
        if (booking.getStart().before(new java.sql.Date(System.currentTimeMillis()))) {
            throw new ValidationException("Дата начала бронирования не может быть раньше текущей даты");
        }
        if (!foundItem.get().getAvailable()) {
            throw new ValidationException(String.format("Предмет (id=%d) недоступен для бронирования", booking.getItem().getId()));
        }
        booking.setStatus(BookingStatus.WAITING);
        return bookingRepository.save(booking);
    }

    @Override
    public Booking approveBooking(Integer userId, Integer bookingId, boolean approved) {
        Optional<Booking> foundBooking = bookingRepository.findById(bookingId);
        if (foundBooking.isEmpty()) {
            throw new NotFoundException(String.format("Бронирование (id=%d) не найдено", bookingId));
        }
        Booking booking = foundBooking.get();
        if (booking.getStatus() != BookingStatus.WAITING) {
            throw new ValidationException("Бронирование должно быть в статусе WAITING");
        }
        if (!Objects.equals(booking.getItem().getOwner().getId(), userId)) {
            throw new ValidationException(String.format("Пользователь (id=%d) не является владельцем предмета (id=%d)", userId, booking.getItem().getId()));
        }
        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        return bookingRepository.save(booking);
    }

    @Override
    public List<Booking> getBookingsByOwner(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(String.format("Пользователь (id=%d) не зарегистрирован", userId));
        }
        return bookingRepository.findAllByItemOwnerId(userId);
    }

    @Override
    public List<Booking> getBookings(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(String.format("Пользователь (id=%d) не зарегистрирован", userId));
        }
        return bookingRepository.findAllByUserId(userId);
    }

    @Override
    public Booking getLastBooking(Integer itemId) {
        return bookingRepository.getLastBooking(itemId);
    }

    @Override
    public Booking getNextBooking(Integer itemId) {
        return bookingRepository.getNextBooking(itemId);
    }

    @Override
    public Booking getBookingByUserId(Integer userId, Integer bookingId) {
        return bookingRepository.getByIdAndUserId(bookingId, userId);
    }
}
