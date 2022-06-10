package com.exadel.coolDesking.workspace;

import com.exadel.coolDesking.common.exception.BadRequestException;
import com.exadel.coolDesking.common.exception.ConflictException;
import com.exadel.coolDesking.common.exception.FileParseException;
import com.exadel.coolDesking.common.exception.NotFoundException;
import com.exadel.coolDesking.floorPlan.FloorPlan;
import com.exadel.coolDesking.floorPlan.FloorPlanRepository;
import com.exadel.coolDesking.workspace.projection.classBasedProjection.WorkplaceProjection;
import lombok.RequiredArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

@RequiredArgsConstructor
@Service
public class WorkplaceService {
    private final WorkplaceRepository workplaceRepository;
    private final FloorPlanRepository floorPlanRepository;
    private final WorkplaceMapper workplaceMapper;
    private final KafkaTemplate<String, NotFoundException> kafkaTemplate;

    public List<WorkplaceProjection> getWorkplaceNumberByInterfaceProjection(UUID floorPlanId){
        return workplaceRepository.findAllByFloorPlan_Id(floorPlanId);
    }

    public List<WorkplaceProjection> getWorkplaceNumberByClassProjection(UUID floorPlanId){
        return workplaceRepository.findAllByFloorPlan_Id(floorPlanId);
    }

    public List<WorkplaceResponseDto> getWorkPlaces(UUID officeId, WorkplaceFilter filter) {
        List<WorkplaceResponseDto> workplaceResponseDtos = new ArrayList<>();
        filter.setOfficeId(officeId);
        List<Workplace> workplaces = workplaceRepository.findAll(filter);
        workplaces.forEach(workplace -> {
            WorkplaceResponseDto workplaceResponseDto = workplaceMapper.entityToResponseDTO(workplace);
            workplaceResponseDtos.add(workplaceResponseDto);
        });
        return workplaceResponseDtos;
    }

    public WorkplaceResponseDto getWorkPlace(UUID officeId, UUID workplaceId) {
        Workplace workplace = workplaceRepository.findByFloorPlan_Office_IdAndId(officeId, workplaceId)
                .orElseThrow(() -> new NotFoundException("There is no such a workplace", Workplace.class, "officeId and workplaceId"));

        return workplaceMapper.entityToResponseDTO(workplace);
    }

    public void addWorkplaces(MultipartHttpServletRequest file, UUID officeId, UUID floorPlanId) {
        final Iterator<String> fileNames = file.getFileNames();
        MultipartFile multipartFile = file.getFile(fileNames.next());
        if (multipartFile == null) {
            throw new BadRequestException("Invalid ContentType", Workplace.class, "multipartFile");
        }
        String contentType = multipartFile.getContentType();
        if ((contentType == null) || (!contentType.equals("application/vnd.ms-excel") && !contentType.equals("text/csv"))) {
            throw new BadRequestException("Invalid ContentType", Workplace.class, "contentType");
        }

        FloorPlan floorPlan = floorPlanRepository.findById(floorPlanId)
                .orElseThrow(() -> new NotFoundException(floorPlanId + " does not exist", Workplace.class, "floorPlanId"));

        List<Workplace> workplaces;
        try (InputStream inputStream = multipartFile.getInputStream()) {
            if (contentType.equals("application/vnd.ms-excel")) {
                workplaces = readFromXls(inputStream, officeId, floorPlanId, floorPlan);
            } else {
                workplaces = readFromCsv(inputStream, officeId, floorPlanId, floorPlan);
            }
        } catch (IOException e) {
            throw new FileParseException("Unable to read file", Workplace.class, "file");
        }
        workplaceRepository.saveAll(workplaces);
    }

    public WorkplaceResponseDto addWorkplace(UUID officeId, UUID floorPlanId, WorkplaceCreateDto workplaceCreateDto) {
        boolean existWorkplace = workplaceRepository.existsByFloorPlan_Office_IdAndWorkplaceNumber(officeId, workplaceCreateDto.getWorkplaceNumber());
        if (existWorkplace) {
            kafkaTemplate.send("NotFoundException", new NotFoundException("There is already such workplace", Workplace.class, "workplaceNumber"));
        }
        Optional<FloorPlan> byId = floorPlanRepository.findById(floorPlanId);
        FloorPlan floorPlan = floorPlanRepository.findById(floorPlanId)
                .orElseThrow(() -> new NotFoundException(floorPlanId + " does not exist", Workplace.class, "floorPlanId"));

        Workplace workplace = workplaceMapper.workplaceCreateDtoToEntity(workplaceCreateDto);
        workplace.setStatus(WorkplaceStatus.AVAILABLE);
        workplace.setFloorPlan(floorPlan);
        Workplace savedWorkplace = workplaceRepository.save(workplace);

//        kafkaTemplate.send("MyTopic", savedWorkplace.toString());

        return workplaceMapper.entityToResponseDTO(savedWorkplace);
    }

    public WorkplaceResponseDto editWorkPlace(UUID officeId, UUID workplaceId, WorkplaceUpdateDto workplaceUpdateDto) {
        Workplace workplace = workplaceRepository.findByFloorPlan_Office_IdAndId(officeId, workplaceId)
                .orElseThrow(() -> new NotFoundException("There is no such a workplace", Workplace.class, "officeId and workplaceId"));

        workplaceMapper.updateEntity(workplaceUpdateDto, workplace);
        Workplace savedWorkplace = workplaceRepository.save(workplace);

        return workplaceMapper.entityToResponseDTO(savedWorkplace);
    }

    public List<Integer> findAllWorkplacesByFloorPlan(FloorPlan floorPlan) {
        return workplaceRepository.findAllByFloorPlan(floorPlan);
    }

    private List<Workplace> readFromXls(InputStream inputStream, UUID officeId, UUID floorPlanId, FloorPlan floorPlan) {
        List<Workplace> workplaces = new ArrayList<>();
        HSSFWorkbook workbook;
        try {
            workbook = new HSSFWorkbook(inputStream);
        } catch (IOException e) {
            throw new FileParseException("Enable to read file", Workplace.class, "file");
        }
        Sheet sheet = workbook.getSheetAt(0);
        int skip = 0;
        for (Row row : sheet) {
            skip++;
            if (skip == 1) continue;
            WorkplaceCreateDto workplaceCreateDto = new WorkplaceCreateDto();
            workplaceCreateDto.setWorkplaceNumber((int) row.getCell(0).getNumericCellValue()); // workPlaceNumber
            workplaceCreateDto.setType(WorkplaceType.valueOf(row.getCell(1).getStringCellValue())); // type
            workplaceCreateDto.setIsNextToWindow(Boolean.valueOf(row.getCell(2).getStringCellValue())); // nextToWindow
            workplaceCreateDto.setHasPc(Boolean.valueOf(row.getCell(3).getStringCellValue())); // hasPc
            workplaceCreateDto.setHasMonitor(Boolean.valueOf(row.getCell(4).getStringCellValue())); // hasMonitor
            workplaceCreateDto.setHasKeyboard(Boolean.valueOf(row.getCell(5).getStringCellValue())); // hasKeyboard
            workplaceCreateDto.setHasMouse(Boolean.valueOf(row.getCell(6).getStringCellValue())); // hasMouse
            workplaceCreateDto.setHasHeadSet(Boolean.valueOf(row.getCell(7).getStringCellValue())); // hasHeadset

            boolean existsByWorkplaceNumber = workplaceRepository.existsByFloorPlan_Office_IdAndWorkplaceNumber(officeId, workplaceCreateDto.getWorkplaceNumber());
            if (existsByWorkplaceNumber) {
                throw new ConflictException(workplaceCreateDto.getWorkplaceNumber() + " is not unique within the " + floorPlanId, Workplace.class, "floorPlanId");
            }

            Workplace workplace = workplaceMapper.workplaceCreateDtoToEntity(workplaceCreateDto);
            workplace.setStatus(WorkplaceStatus.AVAILABLE);
            workplace.setFloorPlan(floorPlan);
            workplaces.add(workplace);
        }
        return workplaces;
    }

    private List<Workplace> readFromCsv(InputStream inputStream, UUID officeId, UUID floorPlanId, FloorPlan floorPlan) {
        List<Workplace> workplaces = new ArrayList<>();
        BufferedReader br;
        String line;

        br = new BufferedReader(new InputStreamReader(inputStream));
        int skip = 0;
        while (true) {
            try {
                if ((line = br.readLine()) == null) break;
            } catch (IOException e) {
                throw new FileParseException("Enable to read file", Workplace.class, "file");
            }
            String[] split = line.trim().split(",");
            if (split.length != 8) {
                throw new ConflictException("Column is missing", Workplace.class, "Column");
            }
            WorkplaceCreateDto workplaceCreateDto = new WorkplaceCreateDto();
            skip++;
            if (skip == 1) continue;
            workplaceCreateDto.setWorkplaceNumber(Integer.valueOf(split[0])); // workPlaceNumber
            workplaceCreateDto.setType(WorkplaceType.valueOf(split[1])); // type
            workplaceCreateDto.setIsNextToWindow(Boolean.valueOf(split[2])); // nextToWindow
            workplaceCreateDto.setHasPc(Boolean.valueOf(split[3])); // hasPc
            workplaceCreateDto.setHasMonitor(Boolean.valueOf(split[4])); // hasMonitor
            workplaceCreateDto.setHasKeyboard(Boolean.valueOf(split[5])); // hasKeyboard
            workplaceCreateDto.setHasMouse(Boolean.valueOf(split[6])); // hasMouse
            workplaceCreateDto.setHasHeadSet(Boolean.valueOf(split[7])); // hasHeadset

            boolean existsByWorkplaceNumber = workplaceRepository.existsByFloorPlan_Office_IdAndWorkplaceNumber(officeId, workplaceCreateDto.getWorkplaceNumber());
            if (existsByWorkplaceNumber) {
                throw new ConflictException(workplaceCreateDto.getWorkplaceNumber() + " is not unique within the " + floorPlanId, Workplace.class, "floor_plan_id");
            }
            Workplace workplace = workplaceMapper.workplaceCreateDtoToEntity(workplaceCreateDto);
            workplace.setStatus(WorkplaceStatus.AVAILABLE);
            workplace.setFloorPlan(floorPlan);
            workplaces.add(workplace);
        }
        return workplaces;
    }

}
