package com.exadel.coolDesking.report;

import com.exadel.coolDesking.common.exception.ExceptionResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
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
public class ReportControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private static final ObjectMapper OBJECT_MAPPER = new com.fasterxml.jackson.databind.ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL).registerModule(new JavaTimeModule());


    @Sql(scripts = {"classpath:/sql/insert-office.sql", "classpath:/sql/insert-sample-floor-plan.sql", "classpath:/sql/insert-workplace.sql", "classpath:/sql/insert-user-for-report.sql", "classpath:/sql/insert-booking-for-report.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:/sql/delete-all-bookings.sql", "classpath:/sql/delete-all-users.sql", "classpath:/sql/delete-workplace.sql", "classpath:/sql/delete-floor-plan.sql", "classpath:/sql/delete-office.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    @DisplayName("test on get report with only userId by officeId")
    public void getReportWithEmptyUserByOfficeId() throws Exception {
        UUID officeId = UUID.fromString("11111111-0000-0000-0000-111111111111");
        UUID floorPlanId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        String startDate = "2022-01-01";
        String endDate = "2022-04-01";
        MvcResult request = getRequest(HttpMethod.GET, URI.create("/report/office/" + officeId + "/full/0/start/" + startDate + "/finish/" + endDate), status().isOk());
        String contentAsString = request.getResponse().getContentAsString();
        ReportResponseDtos<ReportResponseDtoWithUserId> reportResponseDtos = OBJECT_MAPPER.readValue(contentAsString, new TypeReference<ReportResponseDtos<ReportResponseDtoWithUserId>>() {
        });
        checkGetReportByOfficeIdWithEmptyUser(reportResponseDtos, floorPlanId, officeId);
    }

    private MvcResult getRequest(HttpMethod method, URI uri, ResultMatcher status) throws Exception {
        return this.mockMvc.perform(
                        MockMvcRequestBuilders.request(method, uri)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status).andReturn();
    }

    private void checkGetReportByOfficeIdWithEmptyUser(ReportResponseDtos<ReportResponseDtoWithUserId> responseDtos, UUID floorPlanId, UUID officeId) {
        ReportResponseDtoWithUserId response = responseDtos.getResponseDtos().get(0);
        assertNotNull(responseDtos);
        assertNotNull(response.getWorkplace());
        assertNotNull(response.getUser());
        assertEquals(response.getStartDate(), LocalDate.parse("2022-03-01"));
        assertEquals(response.getFinishDate(), LocalDate.parse("2022-03-30"));
        assertEquals(response.getFloorPlanId(), floorPlanId);
        assertEquals(response.getOfficeId(), officeId);
    }

    @Sql(scripts = {"classpath:/sql/insert-office.sql", "classpath:/sql/insert-sample-floor-plan.sql", "classpath:/sql/insert-workplace.sql", "classpath:/sql/insert-user-for-report.sql", "classpath:/sql/insert-booking-for-report.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:/sql/delete-all-bookings.sql", "classpath:/sql/delete-all-users.sql", "classpath:/sql/delete-workplace.sql", "classpath:/sql/delete-floor-plan.sql", "classpath:/sql/delete-office.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    @DisplayName("test on get report with full user by officeId")
    public void getReportWithFullUserByOfficeId() throws Exception {
        UUID officeId = UUID.fromString("11111111-0000-0000-0000-111111111111");
        UUID floorPlanId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        String startDate = "2022-01-01";
        String endDate = "2022-04-01";
        MvcResult request = getRequest(HttpMethod.GET, URI.create("/report/office/" + officeId + "/full/1/start/" + startDate + "/finish/" + endDate), status().isOk());
        String contentAsString = request.getResponse().getContentAsString();
        ReportResponseDtos<ReportResponseDtoWithFullUser> reportResponseDtos = OBJECT_MAPPER.readValue(contentAsString, new TypeReference<ReportResponseDtos<ReportResponseDtoWithFullUser>>() {
        });
        checkGetReportByOfficeIdWithFullUser(reportResponseDtos, floorPlanId, officeId);
    }

    private void checkGetReportByOfficeIdWithFullUser(ReportResponseDtos<ReportResponseDtoWithFullUser> responseDtos, UUID floorPlanId, UUID officeId) {
        ReportResponseDtoWithFullUser response = responseDtos.getResponseDtos().get(0);
        assertNotNull(responseDtos);
        assertNotNull(response.getWorkplace());
        assertNotNull(response.getUser());
        assertEquals(response.getUser().getFirstName(), "John");
        assertEquals(response.getUser().getLast_name(), "Terry");
        assertEquals(response.getUser().getRole(), "ADMIN");
        assertEquals(response.getUser().getEmail(), "john@gmail.com");
        assertEquals(response.getStartDate(), LocalDate.parse("2022-03-01"));
        assertEquals(response.getFinishDate(), LocalDate.parse("2022-03-30"));
        assertEquals(response.getFloorPlanId(), floorPlanId);
        assertEquals(response.getOfficeId(), officeId);
    }

    @Sql(scripts = {"classpath:/sql/insert-office.sql", "classpath:/sql/insert-sample-floor-plan.sql", "classpath:/sql/insert-workplace.sql", "classpath:/sql/insert-user-for-report.sql", "classpath:/sql/insert-booking-for-report.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:/sql/delete-all-bookings.sql", "classpath:/sql/delete-all-users.sql", "classpath:/sql/delete-workplace.sql", "classpath:/sql/delete-floor-plan.sql", "classpath:/sql/delete-office.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    @DisplayName("test on get report with only userId by officeId and floorPlanId")
    public void getReportWithEmptyUserByOfficeIdAndFloorPlanId() throws Exception {
        UUID officeId = UUID.fromString("11111111-0000-0000-0000-111111111111");
        UUID floorPlanId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        String startDate = "2022-01-01";
        String endDate = "2022-04-01";
        MvcResult request = getRequest(HttpMethod.GET, URI.create("/report/office/" + officeId + "/floor/" + floorPlanId + "/full/0/start/" + startDate + "/finish/" + endDate), status().isOk());
        String contentAsString = request.getResponse().getContentAsString();
        ReportResponseDtos<ReportResponseDtoWithUserId> reportResponseDtos = OBJECT_MAPPER.readValue(contentAsString, new TypeReference<ReportResponseDtos<ReportResponseDtoWithUserId>>() {
        });
        checkGetReportByOfficeIdWithEmptyUser(reportResponseDtos, floorPlanId, officeId);
    }

    @Sql(scripts = {"classpath:/sql/insert-office.sql", "classpath:/sql/insert-sample-floor-plan.sql", "classpath:/sql/insert-workplace.sql", "classpath:/sql/insert-user-for-report.sql", "classpath:/sql/insert-booking-for-report.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:/sql/delete-all-bookings.sql", "classpath:/sql/delete-all-users.sql", "classpath:/sql/delete-workplace.sql", "classpath:/sql/delete-floor-plan.sql", "classpath:/sql/delete-office.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    @DisplayName("test on get report with full user by officeId")
    public void getReportWithFullUserByOfficeIdAndFloorPlanId() throws Exception {
        UUID officeId = UUID.fromString("11111111-0000-0000-0000-111111111111");
        UUID floorPlanId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        String startDate = "2022-01-01";
        String endDate = "2022-04-01";
        MvcResult request = getRequest(HttpMethod.GET, URI.create("/report/office/" + officeId + "/floor/" + floorPlanId + "/full/1/start/" + startDate + "/finish/" + endDate), status().isOk());
        String contentAsString = request.getResponse().getContentAsString();
        ReportResponseDtos<ReportResponseDtoWithFullUser> reportResponseDtos = OBJECT_MAPPER.readValue(contentAsString, new TypeReference<ReportResponseDtos<ReportResponseDtoWithFullUser>>() {
        });
        checkGetReportByOfficeIdWithFullUser(reportResponseDtos, floorPlanId, officeId);
    }

    @Sql(scripts = {"classpath:/sql/insert-office.sql", "classpath:/sql/insert-sample-floor-plan.sql", "classpath:/sql/insert-workplace.sql", "classpath:/sql/insert-user-for-report.sql", "classpath:/sql/insert-booking-for-report.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:/sql/delete-all-bookings.sql", "classpath:/sql/delete-all-users.sql", "classpath:/sql/delete-workplace.sql", "classpath:/sql/delete-floor-plan.sql", "classpath:/sql/delete-office.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    @DisplayName("test on get report with full user by officeId")
    public void checkNoContentException() throws Exception {
        UUID officeId = UUID.fromString("11111111-0000-0000-0000-111111111111");
        UUID floorPlanId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        String startDate = "2022-10-01";
        String endDate = "2022-12-01";
        MvcResult request = getRequest(HttpMethod.GET, URI.create("/report/office/" + officeId + "/floor/" + floorPlanId + "/full/1/start/" + startDate + "/finish/" + endDate), status().isNoContent());
        String contentAsString = request.getResponse().getContentAsString();
        ExceptionResponse exceptionResponse = OBJECT_MAPPER.readValue(contentAsString, ExceptionResponse.class);
        assertEquals(exceptionResponse.getErrorMessage(), "There are no any bookings");
        assertEquals(exceptionResponse.getField(), "booking");
    }
}
