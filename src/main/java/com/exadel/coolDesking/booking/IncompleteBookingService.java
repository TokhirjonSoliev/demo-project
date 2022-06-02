package com.exadel.coolDesking.booking;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class IncompleteBookingService {
    private final IncompleteBookingRepository incompleteBookingRepository;
    private final String NOT_FOUND_MSG = "NO SUCH BOOKING. SOMETHING WENT WRONG!";

    public InCompleteBooking create(InCompleteBooking inCompleteBooking) {
        return incompleteBookingRepository.save(inCompleteBooking);
    }

    public void deleteAny(UUID createdById) {
        incompleteBookingRepository.deleteAllByCreatedById(createdById);
    }
    /*public void deleteByCreatedById(UUID createdById, String telegramId) {
        if (!incompleteBookingRepository.existsByCreatedById(createdById)) {
            throw new BotNotFoundException(
                    NOT_FOUND_MSG,
                    IncompleteBookingService.class,
                    telegramId
            );
        }
        incompleteBookingRepository.deleteAllByCreatedById(createdById);
    }*/

    /*public void update(InCompleteBooking inCompleteBooking, String telegramId) {
        if (incompleteBookingRepository.existsById(inCompleteBooking.getId())) {
            incompleteBookingRepository.save(inCompleteBooking);
            return;
        }

        throw new BotNotFoundException(
                NOT_FOUND_MSG,
                IncompleteBookingService.class,
                telegramId);
    }*/

    /*public InCompleteBooking getById(UUID id, String telegramId) {
        return incompleteBookingRepository.findIncompleteBookingById(id)
                .orElseThrow(() -> new BotNotFoundException(
                NOT_FOUND_MSG,
                IncompleteBookingService.class,
                telegramId));
    }*/
}
