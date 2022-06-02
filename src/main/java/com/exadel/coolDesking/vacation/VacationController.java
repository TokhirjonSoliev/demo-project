package com.exadel.coolDesking.vacation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping("/vacation")
@RestController
public class VacationController {

    private final VacationService vacationService;

    @PostMapping
    public ResponseEntity<?> save(@RequestBody @Valid VacationCreateDto vacationCreateDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(vacationService.createVacation(vacationCreateDto));
    }

    @GetMapping
    public ResponseEntity<?> getVacations() {
        return ResponseEntity.ok(vacationService.getVacations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getVacationById(@PathVariable UUID id) {
        return ResponseEntity.ok(vacationService.getVacation(id));

    }

    @PutMapping("/{id}")
    public ResponseEntity<?> edit(@PathVariable UUID id, @RequestBody @Valid VacationUpdateDto vacationUpdateDto) {
        return ResponseEntity.ok(vacationService.editVacation(vacationUpdateDto, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        vacationService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }
}
