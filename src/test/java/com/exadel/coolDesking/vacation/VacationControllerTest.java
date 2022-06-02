package com.exadel.coolDesking.vacation;

import com.exadel.coolDesking.common.exception.ExceptionResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.net.URI;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class VacationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private static final com.fasterxml.jackson.databind.ObjectMapper objectMapper = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL).registerModule(new JavaTimeModule());

    @Test
    @DisplayName("Vacation add/create controller test")
    @Sql(scripts = {"classpath:/sql/insert-office.sql", "classpath:/sql/insert-sample-floor-plan.sql",
            "classpath:/sql/insert-workplaceForVacation.sql", "classpath:/sql/insert-userForVacation.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:/sql/delete-vacation.sql", "classpath:/sql/delete-all-users.sql",
            "classpath:/sql/delete-workplace.sql", "classpath:/sql/delete-floor-plan.sql", "classpath:/sql/delete-office.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)

    public void create() throws Exception {
        UUID id = UUID.fromString("11110000-1100-0011-1111-111111000000");
        LocalDate localDate1 = LocalDate.parse("2020-01-01");
        LocalDate localDate2 = LocalDate.parse("2021-02-02");
        var vacationCreateDto = new VacationCreateDto(id, localDate1, localDate2);
        var uri = URI.create("/vacation");
        MvcResult request = createRequest(HttpMethod.POST, uri, vacationCreateDto, status().isCreated());
        String contentAsString = request.getResponse().getContentAsString();
        VacationCreateResponseDto response = objectMapper.readValue(contentAsString, VacationCreateResponseDto.class);
        checkCreateResponse(response);
    }

    public void checkCreateResponse(VacationCreateResponseDto response) {
        assertNotNull(response);
    }


    @Test
    @DisplayName("Vacation add/create controller test")
    @Sql(scripts = {"classpath:/sql/insert-office.sql", "classpath:/sql/insert-sample-floor-plan.sql",
            "classpath:/sql/insert-workplaceForVacation.sql", "classpath:/sql/insert-userForVacation.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:/sql/delete-vacation.sql", "classpath:/sql/delete-all-users.sql",
            "classpath:/sql/delete-workplace.sql", "classpath:/sql/delete-floor-plan.sql", "classpath:/sql/delete-office.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void createVacationUserNotFound() throws Exception {
        UUID id = UUID.fromString("b3201586-ebee-48ed-b68c-67977d93bd79");
        LocalDate localDate1 = LocalDate.parse("2020-01-01");
        LocalDate localDate2 = LocalDate.parse("2021-02-02");
        var vacationCreateDto = new VacationCreateDto(id, localDate1, localDate2);
        var uri = URI.create("/vacation");
        MvcResult request = createRequest(HttpMethod.POST, uri, vacationCreateDto, status().isNotFound());
        String contentAsString = request.getResponse().getContentAsString();
        ExceptionResponse response = objectMapper.readValue(contentAsString, ExceptionResponse.class);
        checkCreateUserNotFoundResponse(response);
    }

    public void checkCreateUserNotFoundResponse(ExceptionResponse response) {
        assertNotNull(response);
        assertEquals(response.getField(), "userId");
        assertEquals(response.getErrorMessage(), "User not found");

    }

    public MvcResult createRequest(HttpMethod method, URI uri, VacationCreateDto vacationCreateDto, ResultMatcher status) throws Exception {
        return this.mockMvc.perform(
                        MockMvcRequestBuilders.request(method, uri)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(vacationCreateDto)))
                .andExpect(status)
                .andReturn();
    }

    @Test
    @DisplayName("get vacation by id")
    @Sql(scripts = {"classpath:/sql/insert-office.sql", "classpath:/sql/insert-sample-floor-plan.sql",
            "classpath:/sql/insert-workplaceForVacation.sql", "classpath:/sql/insert-userForVacation.sql", "classpath:/sql/insert-vacationForVacation.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:/sql/delete-vacation.sql", "classpath:/sql/delete-all-users.sql",
            "classpath:/sql/delete-workplace.sql", "classpath:/sql/delete-floor-plan.sql", "classpath:/sql/delete-office.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)

    public void getVacationById() throws Exception {
        UUID vacationId = UUID.fromString("11110000-1100-0011-1111-111111000000");
        MvcResult request = getRequest(HttpMethod.GET, URI.create("/vacation/" + vacationId), status().isOk());
        String contentAsString = request.getResponse().getContentAsString();
        VacationResponseDto vacationResponseDto = objectMapper.readValue(contentAsString, VacationResponseDto.class);
        checkGetVacationParameters(vacationResponseDto, vacationId);
    }

    private void checkGetVacationParameters(VacationResponseDto vacationResponseDto, UUID vacationId) {
        assertNotNull(vacationResponseDto);
        assertEquals(vacationResponseDto.getVacationId(), vacationId);

    }
    private MvcResult getRequest(HttpMethod method, URI uri, ResultMatcher status) throws Exception {
        return this.mockMvc.perform(
                        MockMvcRequestBuilders.request(method, uri)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status).andReturn();
    }

    @Test
    @DisplayName(value = "get vacation error by id")
    @Sql(scripts = {"classpath:/sql/insert-office.sql", "classpath:/sql/insert-sample-floor-plan.sql",
            "classpath:/sql/insert-workplaceForVacation.sql", "classpath:/sql/insert-userForVacation.sql", "classpath:/sql/insert-vacationForVacation.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:/sql/delete-vacation.sql", "classpath:/sql/delete-all-users.sql",
            "classpath:/sql/delete-workplace.sql", "classpath:/sql/delete-floor-plan.sql", "classpath:/sql/delete-office.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)

    public void getVacationByIdNotFoundException() throws Exception {
        UUID vacationId = UUID.fromString("338ad4f6-beac-4736-81be-4f363a5d1b07");
        URI uri = URI.create("/vacation/" + vacationId);
        MvcResult request = getRequest(HttpMethod.GET, uri, status().isNotFound());

        String contentAsString = request.getResponse().getContentAsString();
        ExceptionResponse response = objectMapper.readValue(contentAsString, ExceptionResponse.class);
        checkErrorFindByIdResponse(response);
    }

    private void checkErrorFindByIdResponse(ExceptionResponse response) {
        assertNotNull(response);
        assertEquals(response.getField(), "vacationId");
        assertEquals(response.getErrorMessage(), "Vacation not found");
    }
}
