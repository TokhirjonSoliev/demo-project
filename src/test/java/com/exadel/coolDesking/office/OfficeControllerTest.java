package com.exadel.coolDesking.office;

import com.exadel.coolDesking.common.exception.ExceptionResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
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
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class OfficeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);

    @Test
    @DisplayName("Office add/create controller test")
    @Sql(scripts = {"classpath:/sql/delete-office.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void create() throws Exception {
        var office = new OfficeCreateDto(
                "Exadel Tashkent",
                "ExTUz",
                "Uzbekistan",
                "Tashkent",
                "Chilonzor district 9kv",
                100,
                3
        );
        checkErrorResponseNotNull(office);
        URI uri = URI.create("/office");
        MvcResult request = createRequest(uri, office, status().isCreated());

        String contentAsString = request.getResponse().getContentAsString();
        Office response = OBJECT_MAPPER.readValue(contentAsString, Office.class);
        checkResponse(response);
    }

    private void checkResponse(Office response) {
        assertNotNull(response);
        assertNotNull(response.getId());
    }

    private MvcResult createRequest(URI uri, OfficeCreateDto officeCreateDto, ResultMatcher status) throws Exception {
        return this.mockMvc.perform(
                        MockMvcRequestBuilders.request(HttpMethod.POST, uri)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(OBJECT_MAPPER.writeValueAsBytes(officeCreateDto)))
                .andExpect(status).andReturn();
    }

    @Test
    @DisplayName(value = "creating office with this name already exist")
    @Sql(scripts = {"classpath:/sql/insert-office.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:/sql/delete-office.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void createOfficeAlreadyExistError() throws Exception {
        var office = new OfficeCreateDto(
                "Exadel Tashkent",
                "ExTUz",
                "Uzbekistan",
                "Tashkent",
                "Chilonzor district 9kv",
                100,
                3
        );
        checkErrorResponseNotNull(office);
        URI uri = URI.create("/office");
        MvcResult request = createRequest(uri, office, status().isConflict());

        String contentAsString = request.getResponse().getContentAsString();
        checkErrorResponse(OBJECT_MAPPER.readValue(contentAsString, ExceptionResponse.class));
    }

    private void checkErrorResponse(ExceptionResponse response) {
        assertNotNull(response);
        assertEquals(response.getField(), "name");
        assertEquals(response.getErrorMessage(), "Office with this name already exists");
    }

    private void checkErrorResponseNotNull(OfficeCreateDto officeCreateDto) {
        assertNotNull(officeCreateDto);
        assertNotNull(officeCreateDto.getName());
        assertNotNull(officeCreateDto.getAddress());
        assertNotNull(officeCreateDto.getCity());
        assertNotNull(officeCreateDto.getCountry());
        assertNotNull(officeCreateDto.getFloorCount());
        assertNotNull(officeCreateDto.getShortName());
        assertNotNull(officeCreateDto.getParkingCapacity());
    }

    @Test
    @DisplayName(value = "get office by id")
    @Sql(scripts = {"classpath:/sql/insert-office.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:/sql/delete-office.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getOfficeById() throws Exception {
        UUID officeId = UUID.fromString("11111111-0000-0000-0000-111111111111");
        MvcResult request = getRequest(HttpMethod.GET, URI.create("/office/" + officeId), status().isOk());

        String contentAsString = request.getResponse().getContentAsString();
        OfficeResponse officeResponse = OBJECT_MAPPER.readValue(contentAsString, OfficeResponse.class);
        checkGetOfficeParameters(officeResponse, officeId);
    }

    private void checkGetOfficeParameters(OfficeResponse officeResponse, UUID officeId) {
        assertNotNull(officeResponse);
        assertEquals(officeResponse.getId(), officeId);
    }

    private MvcResult getRequest(HttpMethod method, URI uri, ResultMatcher status) throws Exception {
        return this.mockMvc.perform(
                        MockMvcRequestBuilders.request(method, uri)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status).andReturn();
    }

    @Test
    @DisplayName(value = "get office by id")
    @Sql(scripts = {"classpath:/sql/insert-office.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:/sql/delete-office.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getOfficeByIdNotFoundException() throws Exception {
        UUID officeId = UUID.fromString("22222222-0000-0000-0000-222222222222");
        URI uri = URI.create("/office/" + officeId);
        MvcResult request = getRequest(HttpMethod.GET, uri, status().isNotFound());

        String contentAsString = request.getResponse().getContentAsString();
        ExceptionResponse response = OBJECT_MAPPER.readValue(contentAsString, ExceptionResponse.class);
        checkErrorFindByIdResponse(response);
    }

    private void checkErrorFindByIdResponse(ExceptionResponse response) {
        assertNotNull(response);
        assertEquals(response.getField(), "id");
        assertEquals(response.getErrorMessage(), "Office not found");
    }

    @Test
    @DisplayName(value = "office delete by id")
    @Sql(scripts = {"classpath:/sql/insert-office.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:/sql/delete-office.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void deleteOfficeById() throws Exception {
        UUID officeId = UUID.fromString("11111111-0000-0000-0000-111111111111");
        URI uri = URI.create("/office/" + officeId);
        MvcResult request = getRequest(HttpMethod.DELETE, uri, status().isOk());

        String contentAsString = request.getResponse().getContentAsString();
        OfficeDeletedDto response = OBJECT_MAPPER.readValue(contentAsString, OfficeDeletedDto.class);
        checkDeleteResponse(response, officeId);
    }

    private void checkDeleteResponse(OfficeDeletedDto response, UUID officeId){
        assertNotNull(response);
        assertEquals(response.getOfficeId(), officeId);
        assertEquals(response.getMessage(), "Office deleted successfully");
    }

    @Test
    @DisplayName(value = "office delete by id")
    public void deleteOfficeByIdNotFound() throws Exception {
        UUID officeId = UUID.fromString("11111111-0000-0000-0000-111111111111");
        URI uri = URI.create("/office/" + officeId);
        MvcResult request = getRequest(HttpMethod.DELETE, uri, status().isNotFound());

        String contentAsString = request.getResponse().getContentAsString();
        ExceptionResponse response = OBJECT_MAPPER.readValue(contentAsString, ExceptionResponse.class);
        checkErrorFindByIdResponse(response);
    }
}
