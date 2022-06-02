package com.exadel.coolDesking.vacation;

import com.exadel.coolDesking.common.exception.NotFoundException;
import com.exadel.coolDesking.user.User;
import com.exadel.coolDesking.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class VacationService {
    private final VacationRepository vacationRepository;
    private final UserRepository userRepository;
    private final VacationMapper vacationMapper;

    public List<VacationResponseDto> getVacations() {
        List<Vacation> vacations = vacationRepository.findAll();
        return vacations.stream().map(vacationMapper::entityToResponseDto).toList();
    }

    public VacationResponseDto getVacation(UUID vacationId) {
        Vacation vacation = vacationRepository.findById(vacationId).orElseThrow(
                () -> new NotFoundException("Vacation not found", Vacation.class, "vacationId"));
        return vacationMapper.entityToResponseDto(vacation);
    }

    public VacationCreateResponseDto createVacation(VacationCreateDto vacationCreateDto) {
        userRepository.findById(vacationCreateDto.getUserId()).orElseThrow(
                () -> new NotFoundException("User not found", User.class, "userId"));
        Vacation vacation = vacationMapper.vacationCreateDtoToEntity(vacationCreateDto);
        Vacation save = vacationRepository.save(vacation);
        return new VacationCreateResponseDto(save.getId());
    }

    public VacationResponseDto editVacation(VacationUpdateDto vacationUpdateDto, UUID vacationId) {
        Vacation vacation = vacationRepository.findById(vacationId).orElseThrow(
                () -> new NotFoundException("Vacation not found", Vacation.class, "vacationId"));
        vacationMapper.vacationUpdateDtoToEntity(vacationUpdateDto, vacation);
        Vacation updatedVacation = vacationRepository.save(vacation);
        return vacationMapper.entityToResponseDto(updatedVacation);
    }

    public void delete(UUID vacationId) {
        vacationRepository.findById(vacationId).orElseThrow(
                () -> new NotFoundException("Vacation not found", Vacation.class, "vacationId"));
        vacationRepository.deleteById(vacationId);
    }

}
