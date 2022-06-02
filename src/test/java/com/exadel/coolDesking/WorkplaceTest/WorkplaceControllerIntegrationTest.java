package com.exadel.coolDesking.WorkplaceTest;

import com.exadel.coolDesking.common.exception.ExceptionResponse;
import com.exadel.coolDesking.workspace.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class WorkplaceControllerIntegrationTest {
    private final String officeId = "11111111-0000-0000-0000-111111111111";
    private final String wrongFloorPlanId = "11111111-1111-0000-0000-111111111111";
    private final String correctFloorPlanId = "11111111-1111-1111-1111-111111111111";

    @Autowired
    private MockMvc mockMvc;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);


    @Sql(scripts = {"classpath:/sql/insert-office.sql", "classpath:/sql/insert-sample-floor-plan.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:/sql/delete-workplace.sql", "classpath:/sql/delete-floor-plan.sql", "classpath:/sql/delete-office.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    @DisplayName("test on add one workplace")
    public void create() throws Exception {
        var workplaceCreateDto = new WorkplaceCreateDto();
        workplaceCreateDto.setWorkplaceNumber(188);
        workplaceCreateDto.setHasHeadSet(false);
        workplaceCreateDto.setHasKeyboard(false);
        workplaceCreateDto.setHasMonitor(false);
        workplaceCreateDto.setHasMouse(true);
        workplaceCreateDto.setHasPc(true);
        workplaceCreateDto.setIsNextToWindow(true);
        workplaceCreateDto.setType(WorkplaceType.REGULAR);

        var uri = URI.create("/office/" + officeId + "/floorPlan/" + correctFloorPlanId + "/workplace");
        MvcResult request = createRequest(HttpMethod.POST, uri, workplaceCreateDto, status().isCreated());

        String contentAsString = request.getResponse().getContentAsString();
        WorkplaceResponseDto response = OBJECT_MAPPER.readValue(contentAsString, WorkplaceResponseDto.class);
        checkCreateResponse(UUID.fromString(correctFloorPlanId), response, workplaceCreateDto);
    }

    @Sql(scripts = {"classpath:/sql/delete-workplace.sql", "classpath:/sql/delete-floor-plan.sql", "classpath:/sql/delete-office.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    @DisplayName("test on catching exception of adding one workplace")
    public void create_NotFoundException() throws Exception {
        var workplaceCreateDto = new WorkplaceCreateDto();
        workplaceCreateDto.setWorkplaceNumber(17);
        workplaceCreateDto.setHasHeadSet(false);
        workplaceCreateDto.setHasKeyboard(false);
        workplaceCreateDto.setHasMonitor(false);
        workplaceCreateDto.setHasMouse(true);
        workplaceCreateDto.setHasPc(true);
        workplaceCreateDto.setIsNextToWindow(true);
        workplaceCreateDto.setType(WorkplaceType.REGULAR);

        var uri = URI.create("/office/" + officeId + "/floorPlan/" + wrongFloorPlanId + "/workplace");
        MvcResult request = createRequest(HttpMethod.POST, uri, workplaceCreateDto, status().isNotFound());

        String contentAsString = request.getResponse().getContentAsString();
        ExceptionResponse response = OBJECT_MAPPER.readValue(contentAsString, ExceptionResponse.class);
        checkErrorNotFoundResponse(response, wrongFloorPlanId);
    }

    public void checkErrorNotFoundResponse(ExceptionResponse response, String floorPlanId) {
        assertNotNull(response);
        assertEquals(response.getField(), "floorPlanId");
        assertEquals(response.getErrorMessage(), floorPlanId + " does not exist");
    }

    public void checkCreateResponse(UUID floorPlanId, WorkplaceResponseDto response, WorkplaceCreateDto request) {
        assertNotNull(response);
        assertNotNull(response.getId());
        assertEquals(response.getFloorPlanId(), floorPlanId);
        assertEquals(response.getHasKeyboard(), request.getHasKeyboard());
        assertEquals(response.getWorkplaceNumber(), request.getWorkplaceNumber());
        assertEquals(response.getHasMonitor(), request.getHasMonitor());
        assertEquals(response.getHasPc(), request.getHasPc());
        assertEquals(response.getHasHeadSet(), request.getHasHeadSet());
        assertEquals(response.getStatus(), WorkplaceStatus.AVAILABLE);
        assertEquals(response.getType(), request.getType());
    }

    public MvcResult createRequest(HttpMethod method, URI uri, WorkplaceCreateDto workplaceCreateDto, ResultMatcher status) throws Exception {
        return this.mockMvc.perform(
                        MockMvcRequestBuilders.request(method, uri)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(OBJECT_MAPPER.writeValueAsBytes(workplaceCreateDto)))
                .andExpect(status)
                .andReturn();
    }

    @Test
    @DisplayName("test on getting one workplace by id")
    @Sql(scripts = {"classpath:/sql/insert-office.sql", "classpath:/sql/insert-sample-floor-plan.sql", "classpath:/sql/insert-workplace.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:/sql/delete-workplace.sql", "classpath:/sql/delete-floor-plan.sql", "classpath:/sql/delete-office.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getWorkplaceById() throws Exception {
        UUID officeId = UUID.fromString("11111111-0000-0000-0000-111111111111");
        UUID workplaceId = UUID.fromString("11111111-0000-0000-0000-111111111111");
        MvcResult request = getRequest(HttpMethod.GET, URI.create("/office/" + officeId + "/workplace/" + workplaceId), status().isOk());

        String contentAsString = request.getResponse().getContentAsString();
        WorkplaceResponseDto response = OBJECT_MAPPER.readValue(contentAsString, WorkplaceResponseDto.class);
        checkGetWorkplaceParameters(response, workplaceId);
    }

    @Test
    @DisplayName("test on catching exception on getting one workplace by id")
    @Sql(scripts = {"classpath:/sql/insert-office.sql", "classpath:/sql/insert-sample-floor-plan.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:/sql/delete-workplace.sql", "classpath:/sql/delete-floor-plan.sql", "classpath:/sql/delete-office.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getWorkplaceByIdNotFoundException() throws Exception {
        UUID officeId = UUID.fromString("11111111-0000-0000-0000-111111111111");
        UUID workplaceId = UUID.fromString("11111111-0000-0000-0000-111111111111");
        MvcResult request = getRequest(HttpMethod.GET, URI.create("/office/" + officeId + "/workplace/" + workplaceId), status().isNotFound());

        String contentAsString = request.getResponse().getContentAsString();
        ExceptionResponse response = OBJECT_MAPPER.readValue(contentAsString, ExceptionResponse.class);
        checkErrorFindByIdResponse(response);
    }

    private void checkErrorFindByIdResponse(ExceptionResponse response) {
        assertNotNull(response);
        assertEquals(response.getField(), "officeId and workplaceId");
        assertEquals(response.getErrorMessage(), "There is no such a workplace");
    }

    private void checkGetWorkplaceParameters(WorkplaceResponseDto responseDto, UUID workplaceId) {
        assertNotNull(responseDto);
        assertEquals(responseDto.getId(), workplaceId);
    }

    private MvcResult getRequest(HttpMethod method, URI uri, ResultMatcher status) throws Exception {
        return this.mockMvc.perform(
                        MockMvcRequestBuilders.request(method, uri)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status).andReturn();
    }

    @Sql(scripts = {"classpath:/sql/insert-office.sql", "classpath:/sql/insert-sample-floor-plan.sql", "classpath:/sql/insert-workplace.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:/sql/delete-workplace.sql", "classpath:/sql/delete-floor-plan.sql", "classpath:/sql/delete-office.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    @DisplayName("test on editing workplace by id")
    public void editWorkplace() throws Exception {
        UUID workplaceId = UUID.fromString("11111111-0000-0000-0000-111111111111");

        var workplaceUpdateDto = new WorkplaceUpdateDto();
        workplaceUpdateDto.setHasHeadSet(false);
        workplaceUpdateDto.setHasKeyboard(false);
        workplaceUpdateDto.setHasMonitor(false);
        workplaceUpdateDto.setHasMouse(true);
        workplaceUpdateDto.setHasPc(true);
        workplaceUpdateDto.setIsNextToWindow(true);
        workplaceUpdateDto.setType(WorkplaceType.REGULAR);
        workplaceUpdateDto.setStatus(WorkplaceStatus.AVAILABLE);

        var uri = URI.create("/office/" + officeId + "/workplace/" + workplaceId);
        MvcResult request = editRequest(HttpMethod.PUT, uri, workplaceUpdateDto, status().isCreated());

        String contentAsString = request.getResponse().getContentAsString();
        WorkplaceResponseDto response = OBJECT_MAPPER.readValue(contentAsString, WorkplaceResponseDto.class);
        checkEditResponse(UUID.fromString(correctFloorPlanId), response, workplaceUpdateDto);
    }

    public MvcResult editRequest(HttpMethod method, URI uri, WorkplaceUpdateDto workplaceUpdateDto, ResultMatcher status) throws Exception {
        return this.mockMvc.perform(
                        MockMvcRequestBuilders.request(method, uri)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(OBJECT_MAPPER.writeValueAsBytes(workplaceUpdateDto)))
                .andExpect(status)
                .andReturn();
    }

    public void checkEditResponse(UUID floorPlanId, WorkplaceResponseDto response, WorkplaceUpdateDto request) {
        assertNotNull(response);
        assertNotNull(response.getId());
        assertEquals(response.getFloorPlanId(), floorPlanId);
        assertEquals(response.getHasKeyboard(), request.getHasKeyboard());
        assertEquals(response.getHasMonitor(), request.getHasMonitor());
        assertEquals(response.getHasPc(), request.getHasPc());
        assertEquals(response.getHasHeadSet(), request.getHasHeadSet());
        assertEquals(response.getStatus(), WorkplaceStatus.AVAILABLE);
        assertEquals(response.getType(), request.getType());
    }

    @Sql(scripts = {"classpath:/sql/insert-office.sql", "classpath:/sql/insert-sample-floor-plan.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:/sql/delete-workplace.sql", "classpath:/sql/delete-floor-plan.sql", "classpath:/sql/delete-office.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    @DisplayName("test on adding workplaces by file")
    public void addWorkplacesByExelFile() throws Exception {
        String url = "http://localhost:8080/office/" + officeId + "/floorPlan/" + correctFloorPlanId + "/workplace/bulk";
        MockMultipartFile toUpload = new MockMultipartFile("file", "test1.xls", "application/vnd.ms-excel", new ClassPathResource("/externalResources/test1.xls").getInputStream());
        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart(url)
                        .file(toUpload);
        this.mockMvc.perform(builder).andExpect(status().isCreated());
    }

    @Sql(scripts = {"classpath:/sql/insert-office.sql", "classpath:/sql/insert-sample-floor-plan.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:/sql/delete-workplace.sql", "classpath:/sql/delete-floor-plan.sql", "classpath:/sql/delete-office.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    @DisplayName("test on catching exception of adding workplaces by file")
    public void addWorkplacesByExelFileException() throws Exception {
        String url = "http://localhost:8080/office/" + officeId + "/floorPlan/" + correctFloorPlanId + "/workplace/bulk";
        MockMultipartFile toUpload = new MockMultipartFile("file", "incorrect.txt", "text/plain", new ClassPathResource("/externalResources/incorrect.txt").getInputStream());
        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart(url)
                        .file(toUpload);
        this.mockMvc.perform(builder).andExpect(status().isBadRequest());
    }
}
