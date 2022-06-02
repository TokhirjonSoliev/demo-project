package com.exadel.coolDesking.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class WaitingListService {
    private final WaitingListRepository waitinglistRepository;
}
