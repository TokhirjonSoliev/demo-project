package com.exadel.coolDesking.booking;

import com.exadel.coolDesking.common.exception.ConflictException;
import com.exadel.coolDesking.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    public List<BookingDTO> getBookings() {
        List<BookingDTO> bookingDtos = new ArrayList<>();
        List<Booking> bookings = bookingRepository.findAll();
        bookings.forEach(booking -> {
            BookingDTO bookingDTO = bookingMapper.entityToResponseDTO(booking);
            bookingDtos.add(bookingDTO);
        });
        return bookingDtos;
    }

    public BookingDTO addBooking(BookingDTO bookingDto) {
        if (checkBooking(bookingDto)) {
            throw new ConflictException("Conflict", Booking.class, "booking");
        }
        Booking booking = bookingMapper.bookingCreateDtoToEntity(bookingDto);
        Booking savedBooking = bookingRepository.save(booking);
        return bookingMapper.entityToResponseDTO(savedBooking);
    }


    public BookingDTO getBooking(UUID id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("There is no such booking", Booking.class, "id"));
        return bookingMapper.entityToResponseDTO(booking);
    }

    public BookingDTO updateBooking(BookingDTO bookingDto, UUID id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("There is no such booking", Booking.class, "id"));

        bookingMapper.updateEntity(bookingDto, booking);
        booking.setId(id);
        Booking savedBookings = bookingRepository.save(booking);
        return bookingMapper.entityToResponseDTO(savedBookings);
    }

    public Boolean deleteBooking(UUID id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("There is no such booking", Booking.class, "id"));

        bookingRepository.delete(booking);
        return true;
    }

    private Boolean checkBooking(BookingDTO bookingDto) {
        List<Booking> bookings = bookingRepository.findBookingByStartDateBetweenAndEndDateBetween(
                bookingDto.getStartDate(),
                bookingDto.getEndDate(),
                bookingDto.getStartDate(),
                bookingDto.getEndDate());
        return !bookings.isEmpty();
    }
}
