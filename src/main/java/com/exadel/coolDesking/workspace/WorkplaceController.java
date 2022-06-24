package com.exadel.coolDesking.workspace;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.validation.Valid;
import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping(path = "/office/{office_id}")
@RestController
public class WorkplaceController {
    private final WorkplaceService workplaceService;

    /**
     * This method returns all workplaces regardless of their status
     *
     * @return List<Workplace>
     */

    @PreAuthorize(value = "hasAnyRole('ADMIN', 'MANAGER', 'MAP_EDITOR', 'COMMON_USER')")
    @GetMapping("/workplace")
    public ResponseEntity<?> getWorkplaces(@PathVariable("office_id") UUID officeId,
                                           @RequestParam(required = false) Integer workplaceNumber,
                                           @RequestParam(required = false) WorkplaceType type,
                                           @RequestParam(required = false) Boolean isNextToWindow,
                                           @RequestParam(required = false) Boolean hasPc,
                                           @RequestParam(required = false) Boolean hasMonitor,
                                           @RequestParam(required = false) Boolean hasKeyboard,
                                           @RequestParam(required = false) Boolean hasMouse,
                                           @RequestParam(required = false) Boolean hasHeadSet,
                                           @RequestParam(required = false) Integer floor,
                                           @RequestParam(required = false) Boolean kitchen,
                                           @RequestParam(required = false) Boolean confRoom) {
        WorkplaceFilter filter = new WorkplaceFilter(workplaceNumber,type, isNextToWindow, hasPc, hasMonitor, hasKeyboard, hasMouse, hasHeadSet, floor, kitchen, confRoom, officeId);
        return ResponseEntity.ok(workplaceService.getWorkPlaces(officeId, filter));
    }


    @PreAuthorize(value = "hasAnyRole('ADMIN', 'MANAGER', 'MAP_EDITOR')")
    @GetMapping("/floorPlan/{floorPlan_id}/workplace/projection")
    public ResponseEntity<?> getWorkplaceNumber(@PathVariable("office_id") UUID officeId, @PathVariable("floorPlan_id") UUID floorPlanId){
        return ResponseEntity.ok(workplaceService.getWorkplaceNumberByClassProjection(floorPlanId));
    }
    /**
     * This method returns workplace which has the same id with param id, otherwise it returns null
     *
     * @param office_id, workplace_id
     * @return Workplace
     */

    @PreAuthorize(value = "hasAnyRole('ADMIN', 'MANAGER', 'MAP_EDITOR', 'COMMON_USER')")
    @GetMapping("/workplace/{workplace_id}")
    public ResponseEntity<?> getWorkplace(@PathVariable UUID office_id, @PathVariable UUID workplace_id) {
        return ResponseEntity.ok(workplaceService.getWorkPlace(office_id, workplace_id));
    }

    /**
     * This method adds a file of new workplace if it does not exist
     *
     * @param file, office_id ,workplace_id
     * @return status created
     */
    @PreAuthorize(value = "hasAnyRole('ADMIN', 'MAP_EDITOR')")
    @PostMapping("/floorPlan/{floorPlan_id}/workplace/bulk")
    public ResponseEntity<?> addWorkplaces(MultipartHttpServletRequest file, @PathVariable UUID office_id, @PathVariable UUID floorPlan_id) {
        workplaceService.addWorkplaces(file, office_id, floorPlan_id);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * This method adds a new workplace
     * @param office_id
     * @param floorPlan_id
     * @param workplaceCreateDto
     * @return status created
     */
    @PreAuthorize(value = "hasAnyRole('ADMIN', 'MAP_EDITOR')")
    @PostMapping("/floorPlan/{floorPlan_id}/workplace")
    public ResponseEntity<?> addWorkplace(@PathVariable UUID office_id, @PathVariable UUID floorPlan_id, @RequestBody @Valid WorkplaceCreateDto workplaceCreateDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(workplaceService.addWorkplace(office_id, floorPlan_id, workplaceCreateDto));
    }

    /**
     * This method edits workplace if it exists, otherwise not
     *
     * @param office_id, workplace_id
     * @return WorkplaceResponseDto
     */
    @PreAuthorize(value = "hasAnyRole('ADMIN', 'MAP_EDITOR')")
    @PutMapping("/workplace/{workplace_id}")
    public ResponseEntity<?> editWorkplace(@PathVariable UUID office_id, @PathVariable UUID workplace_id, @RequestBody @Valid WorkplaceUpdateDto workplaceUpdateDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(workplaceService.editWorkPlace(office_id, workplace_id, workplaceUpdateDto));
    }

}