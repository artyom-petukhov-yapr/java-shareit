package ru.practicum.shareit.booking;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.util.Date;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Booking {
    /**
     * Уникальный идентификатор бронирования
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    /**
     * Пользователь, который осуществляет бронирование
     */
    @ManyToOne
    User booker;

    /**
     * Предмет, который пользователь бронирует
     */
    @ManyToOne(fetch = FetchType.LAZY)
    Item item;

    /**
     * Дата и время начала бронирования
     */
    @Column(name = "start_date")
    Date start;

    /**
     * Дата и время конца бронирования
     */
    @Column(name = "end_date")
    Date end;

    /**
     * Статус бронирования
     */
    @Enumerated(EnumType.STRING)
    BookingStatus status;
}
