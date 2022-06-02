package com.exadel.coolDesking.booking;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class BookingMapper {

    private final ModelMapper mapper;

    public BookingMapper() {
        this.mapper = new ModelMapper();
    }

    public BookingDTO entityToResponseDTO(Booking booking) {
        return mapper.map(booking, BookingDTO.class);
    }

    public Booking bookingCreateDtoToEntity(BookingDTO bookingDTO) {
        return mapper.map(bookingDTO, Booking.class);
    }

    public void updateEntity(BookingDTO bookingDTO, Booking booking) {
        mapper.map(bookingDTO, booking);
    }

    public BookingDTO incompleteToDTO(InCompleteBooking inCompleteBooking) {
        return mapper.map(inCompleteBooking, BookingDTO.class);
    }

}
