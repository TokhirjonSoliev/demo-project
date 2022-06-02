package com.exadel.coolDesking.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping(path = "/booking")
@RestController
public class BookingController {

    private final BookingService bookingService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(bookingService.getBookings());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(bookingService.getBooking(id));
    }

    @PostMapping
    public ResponseEntity<?> addBooking(@RequestBody @Valid BookingDTO bookingDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingService.addBooking(bookingDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBooking(@PathVariable UUID id, @RequestBody @Valid BookingDTO bookingDto) {
        return ResponseEntity.status(HttpStatus.OK).body(bookingService.updateBooking(bookingDto, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBooking(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(bookingService.deleteBooking(id));
    }

}
