package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    // ТЗ: "Получение данных о конкретном бронировании (включая его статус).
    // Может быть выполнено либо автором бронирования, либо владельцем вещи, к которой относится бронирование."
    @Query("select b" +
            " from Booking b" +
            " where b.id = ?1 and (b.booker.id = ?2 or b.item.owner.id = ?2)")
    Booking getByIdAndUserId(Integer bookingId, Integer userId);

    // ТЗ: "Отзыв может оставить только тот пользователь, который брал эту вещь в аренду, и только после
    // окончания срока аренды. Так комментарии будут честными."
    @Query("select count(b) > 0" +
            " from Booking b" +
            " where b.booker.id = ?1 and b.item.id = ?2 and b.end < now() and b.status = 'APPROVED'")
    boolean canUserAddComment(Integer bookerId, Integer itemId);

    List<Booking> findAllByItemOwnerId(Integer ownerId);

    /**
     * Найти все бронирования по userId (автору или владельцу вещи)
     */
    @Query("select b" +
            " from Booking b" +
            " where b.booker.id = ?1 or b.item.owner.id = ?1")
    List<Booking> findAllByUserId(Integer userId);

    /**
     * Найти последнюю бронь по itemId
     */
    @Query("select b" +
            " from Booking b" +
            " where b.item.id = ?1 and b.end < now()" +
            " order by b.end desc")
    Booking getLastBooking(Integer itemId);

    /**
     * Найти следующую бронь по itemId
     */
    @Query("select b" +
        " from Booking b" +
        " where b.item.id = ?1 and b.start > now()" +
        " order by b.start")
    Booking getNextBooking(Integer itemId);
}
