package ru.practicum.shareit.booking;

import java.util.List;

public interface BookingService {
    Booking createBooking(Booking booking);

    Booking approveBooking(Integer userId, Integer bookingId, boolean approved);

    List<Booking> getBookingsByOwner(Integer userId);

    List<Booking> getBookings(Integer userId);

    Booking getLastBooking(Integer itemId);

    Booking getNextBooking(Integer itemId);

    Booking getBookingByUserId(Integer userId, Integer bookingId);
}
