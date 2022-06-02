package com.exadel.coolDesking.floorPlan;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/floor-plan")
public class FloorPlanController {
    private final FloorPlanService floorPlanService;

    @PostMapping
    public ResponseEntity<?> createFloorPlan(@RequestBody @Valid FloorPlanCreateDTO floorPlanCreateDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(floorPlanService.createFloorPlan(floorPlanCreateDTO));
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getFloorPlanById(@PathVariable ("id") UUID id) {
        return ResponseEntity.ok(floorPlanService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateFloorPlan(@RequestBody @Valid FloorPlanUpdateDTO floorPlanUpdateDTO, @PathVariable("id") UUID id) {
       return ResponseEntity.status(HttpStatus.OK).body(floorPlanService.updateFloorPlan(floorPlanUpdateDTO, id));
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") UUID id) {
        floorPlanService.deleteFloorPlan(id);
    }
}
