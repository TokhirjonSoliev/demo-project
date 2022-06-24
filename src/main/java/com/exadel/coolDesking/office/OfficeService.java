package com.exadel.coolDesking.office;

import com.exadel.coolDesking.common.exception.ConflictException;
import com.exadel.coolDesking.common.exception.NotFoundException;
import com.exadel.coolDesking.floorPlan.FloorPlanRepository;
import com.exadel.coolDesking.user.User;
import com.exadel.coolDesking.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Transactional
public class OfficeService {
    private final OfficeRepository officeRepository;
    private final FloorPlanRepository floorPlanRepository;
    private final OfficeMapper officeMapper;
    private final UserService userService;

    public List<Office> getAllOffices() {
        return officeRepository.findAll();
    }

    public OfficeAddedResponse create(OfficeCreateDto officeCreateDto) {
        if (officeRepository.existsByName(officeCreateDto.getName())){
            throw new ConflictException("Office with this name already exists", Office.class, "name");
        }
        UUID officeId = officeRepository.save(officeMapper.officeDtoToOfficeEntity(officeCreateDto)).getId();
        return new OfficeAddedResponse(officeId);
    }

    public OfficeResponse getOfficeById(UUID id) {
        Office office = officeRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Office not found", Office.class, "id")
                );
        OfficeResponse officeResponse = officeMapper.officeToOfficeResponse(office);
        List<UUID> floorIds = floorPlanRepository.findIdsByOfficeId(id);
        officeResponse.setFloorPlanIds(floorIds);
        return officeResponse;
    }

    public List<String> getAllCountries() {
        return officeRepository.findAllCountries();
    }

    public List<String> getCitiesByCountry(String country) {
        return officeRepository.findAllCitiesByCountry(country);
    }

    public List<String> getAllNames(String city) {
        return officeRepository.findOfficeNamesByCity(city);
    }

    public Optional<Office> getOfficeByName(String officeName) {
        return officeRepository.getOfficeByName(officeName);
    }

    public OfficeDeletedDto deleteOffice(UUID id) {
        if (!officeRepository.existsById(id)){
            throw new NotFoundException("Office not found", Office.class, "id");
        }
        List<User> userList = officeRepository.findUserOfficeFloorPlanWorkplace(id);
//        userService.deleteUserPreferredWorkplaceId(userList);
        officeRepository.deleteById(id);
        return new OfficeDeletedDto(id, "Office deleted successfully");
    }
}
