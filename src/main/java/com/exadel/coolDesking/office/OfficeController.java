package com.exadel.coolDesking.office;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/office")
public class OfficeController {
    private final OfficeService officeService;

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(officeService.getOfficeById(id));
    }

    @PostMapping
    public ResponseEntity<?> addOffice(@RequestBody @Valid OfficeCreateDto officeCreateDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(officeService.create(officeCreateDto));
    }

    @GetMapping
    public List<Office> getAllOffices(){
        return officeService.getAllOffices();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOfficeById(@PathVariable UUID id){
        return ResponseEntity.status(HttpStatus.OK).body(officeService.deleteOffice(id));
    }
}
