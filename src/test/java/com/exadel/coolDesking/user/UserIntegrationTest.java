package com.exadel.coolDesking.user;

import com.exadel.coolDesking.common.exception.NotFoundException;
import com.exadel.coolDesking.workspace.Workplace;
import com.exadel.coolDesking.workspace.WorkplaceRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@TestPropertySource("classpath:application.properties")
public class UserIntegrationTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WorkplaceRepository workplaceRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    private String jwtToken;


    @Container
    static PostgreSQLContainer<?> database = new PostgreSQLContainer<>("postgres:12.3")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", database::getJdbcUrl);
        registry.add("spring.datasource.password", database::getPassword);
        registry.add("spring.datasource.username", database::getUsername);
    }

    @BeforeEach
    void takeToken() throws Exception {
        String username = "terry";
        String password = "terry";

        String body = "{\"username\":\"" + username + "\", \"password\":\"" + password + "\"}";

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .content(body))
                .andExpect(status().isOk()).andReturn();

        String header = result.getResponse().getHeader(HttpHeaders.AUTHORIZATION);

        jwtToken = header.replace("Bearer ", "");
    }

    @Sql(scripts = {"classpath:/sql/insert-office.sql", "classpath:/sql/insert-sample-floor-plan.sql", "classpath:/sql/insert-workplace.sql", "classpath:/sql/insert-user-for-report.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:/sql/delete-all-users.sql", "classpath:/sql/delete-workplace.sql", "classpath:/sql/delete-floor-plan.sql", "classpath:/sql/delete-office.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    @DisplayName("test on add one workplace")
    void postUser() throws Exception {

        Workplace workplace = workplaceRepository.findByWorkplaceNumber(1)
                .orElseThrow(() -> new NotFoundException("There is no workplace", Workplace.class, "workplaceNumber"));

        UserDto userDto = new UserDto();
        userDto.setFirstName("Johnn");
        userDto.setUsername("terryy");
        userDto.setPassword("terryy");
        userDto.setLastName("Terryy");
        userDto.setEmail("terryy@gmail.com");
        userDto.setTelegramId("42345");
        userDto.setPreferredWorkplace(workplace.getId());
        userDto.setRole("ADMIN");

        String jsonUser = objectMapper.writeValueAsString(userDto);

        MockHttpServletRequestBuilder requestBuilder = post("/user/add")
                .content(jsonUser)
                .header(HttpHeaders.AUTHORIZATION,
                        "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());

        Optional<User> optionalUser = userRepository.findByUsername(userDto.getUsername());
        Assertions.assertEquals(optionalUser.get().getUsername(), userDto.getUsername());
    }


    @Sql(scripts = {"classpath:/sql/insert-office.sql", "classpath:/sql/insert-sample-floor-plan.sql", "classpath:/sql/insert-workplace.sql", "classpath:/sql/insert-user-for-report.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    void getUserById() throws Exception {

        String userId = "11111111-0000-0000-0000-111111111111";

        mockMvc
                .perform(
                        get("/user/{id}", userId)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }
}

