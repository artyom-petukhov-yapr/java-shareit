package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.config.WebConfig;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;
    private final ModelMapper modelMapper;
    private static final String BOOKING_ID_PATH = "/{bookingId}";

    @PostMapping
    public BookingOutDto createBooking(@RequestHeader(WebConfig.USER_ID_HEADER) Integer userId, @RequestBody BookingInDto booking) {
        booking.setBookerId(userId);
        Booking result = bookingService.createBooking(modelMapper.map(booking, Booking.class));
        return modelMapper.map(result, BookingOutDto.class);
    }

    /**
     * {{baseUrl}}/bookings/{{bookingId}}?approved=true
     */
    @PatchMapping(BOOKING_ID_PATH)
    public BookingOutDto approveBooking(@RequestHeader(WebConfig.USER_ID_HEADER) Integer userId,
                                        @PathVariable Integer bookingId, @RequestParam boolean approved) {
        Booking booking = bookingService.approveBooking(userId, bookingId, approved);
        BookingOutDto bookingOutDto = modelMapper.map(booking, BookingOutDto.class);
        return bookingOutDto;
    }

    @GetMapping(BOOKING_ID_PATH)
    public BookingOutDto getBookingByUserId(@RequestHeader(WebConfig.USER_ID_HEADER) Integer userId, @PathVariable Integer bookingId) {
        Booking booking = bookingService.getBookingByUserId(userId, bookingId);
        return modelMapper.map(booking, BookingOutDto.class);
    }

    @GetMapping("/owner")
    public List<BookingOutDto> getBookingByOwner(@RequestHeader(WebConfig.USER_ID_HEADER) Integer userId) {
        List<Booking> bookings = bookingService.getBookingsByOwner(userId);
        return mapToDtoList(bookings);
    }

    @GetMapping
    public List<BookingOutDto> getBookings(@RequestHeader(WebConfig.USER_ID_HEADER) Integer userId) {
        List<Booking> bookings = bookingService.getBookings(userId);
        return mapToDtoList(bookings);
    }

    /**
     * Выполнить маппинг списка моделей в список dto
     */
    private List<BookingOutDto> mapToDtoList(List<Booking> items) {
        return modelMapper.map(items, new TypeToken<List<BookingOutDto>>() {}.getType());
    }
}
