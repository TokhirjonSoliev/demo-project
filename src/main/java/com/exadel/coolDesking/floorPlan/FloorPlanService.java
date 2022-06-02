package com.exadel.coolDesking.floorPlan;

import com.exadel.coolDesking.common.exception.ConflictException;
import com.exadel.coolDesking.common.exception.NotFoundException;
import com.exadel.coolDesking.office.Office;
import com.exadel.coolDesking.office.OfficeRepository;
import com.exadel.coolDesking.office.Office;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class FloorPlanService {
    private final FloorPlanRepository floorPlanRepository;
    private final OfficeRepository officeRepository;
    private final FloorPlanMapper floorPlanMapper;

    public FloorPlanCreateResponseDTO createFloorPlan(FloorPlanCreateDTO floorPlanCreateDTO) {
        boolean existOffice = officeRepository.existsById(floorPlanCreateDTO.getOfficeId());
        if (existOffice) {
            throw new NotFoundException("Office not found with this id: "
                    +floorPlanCreateDTO.getOfficeId(), FloorPlan.class, "officeId");
        }
        boolean exist = floorPlanRepository.existsByFloorAndOfficeId(
                floorPlanCreateDTO.getFloor(), floorPlanCreateDTO.getOfficeId());
        if (exist) {
            throw new ConflictException("Map with floor " + floorPlanCreateDTO.getFloor() +
                    " already exists in", FloorPlan.class, "floor");
        }
        FloorPlan floorPlan = floorPlanMapper.floorPlanCreateDtoToEntity(floorPlanCreateDTO);
        FloorPlan saveFlorPlan = floorPlanRepository.save(floorPlan);
        return new FloorPlanCreateResponseDTO(saveFlorPlan.getId());
    }

    public FloorPlanResponseDTO getById(UUID id) {
         FloorPlan floorPlan = floorPlanRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("The requested map "+id+" doesn't exist", FloorPlan.class, "id"));
        return floorPlanMapper.floorPlanEntityToResponseDto(floorPlan);
    }

    public FloorPlanResponseDTO updateFloorPlan(FloorPlanUpdateDTO floorPlanUpdateDTO, UUID id) {
        FloorPlan floorPlan = floorPlanRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("The requested map "+id+" doesn't exist",FloorPlan.class, "id"));
        boolean exist = floorPlanRepository.existsByFloorAndOfficeIdAndIdIsNot(
                floorPlanUpdateDTO.getFloor(), floorPlan.getOffice().getId(), id);
        if(exist) {
            throw new ConflictException("Map with floor " + floorPlanUpdateDTO.getFloor() +
                    " already exists in "+floorPlan.getOffice().getName()+" office", FloorPlan.class, "id");
        }
        floorPlanMapper.floorPlanUpdateDtoToEntity(floorPlanUpdateDTO, floorPlan);
        FloorPlan updatedFloorPlan = floorPlanRepository.save(floorPlan);
        return floorPlanMapper.floorPlanEntityToResponseDto(updatedFloorPlan);
    }

    public void deleteFloorPlan(UUID id) {
        boolean exist = floorPlanRepository.existsById(id);
        if (!exist) {
            throw new NotFoundException("The requested map "+id+" doesn't exist",FloorPlan.class, "id");
        }
        floorPlanRepository.deleteById(id);
    }

    public List<Integer> getAllFloorsByOffice(Office office) {
        return floorPlanRepository.getAllFloorsByOffice(office);
    }

    /*public FloorPlan getByOfficeAndFloor(Office office, Integer floor, String chatId) {
        Optional<FloorPlan> floorPlan = floorPlanRepository.findFloorPlanByOfficeAndFloor(office, floor);
        if (floorPlan.isEmpty()) {
            throw new BotNotFoundException(
                    "FloorPlan not found! OFFICE: " + office.getName() + "Floor: " + floor,
                    FloorPlanService.class,
                    chatId
            );
        }
        return floorPlan.get();
    }*/
}
