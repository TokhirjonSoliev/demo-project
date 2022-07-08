package com.exadel.coolDesking.user;

import com.exadel.coolDesking.common.exception.NotFoundException;
import com.exadel.coolDesking.workspace.Workplace;
import com.exadel.coolDesking.workspace.WorkplaceRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
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
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@AutoConfigureMockMvc
@Transactional
@ExtendWith(SpringExtension.class)
public class UserIntegrationTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WorkplaceRepository workplaceRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @Container
    static PostgreSQLContainer<?> database = new PostgreSQLContainer<>("postgres:12.3")
            .withDatabaseName("test")
            .withUsername("duke")
            .withPassword("s3cret");

    @Container
    static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:6.2.1"));

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", database::getJdbcUrl);
        registry.add("spring.datasource.password", database::getPassword);
        registry.add("spring.datasource.username", database::getUsername);
        registry.add("spring.kafka.bootstrap-servers",kafka::getBootstrapServers);
        registry.add("application.jwt.secretKey",()->"secretKey");
        registry.add("application.jwt.tokenPrefix",()->"tokenPrefix");
        registry.add("application.jwt.tokenExpirationAfterDays",()->"14");
    }

    @Sql(scripts = {"classpath:/sql/insert-office.sql", "classpath:/sql/insert-sample-floor-plan.sql", "classpath:/sql/insert-workplace.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
//    @Sql(scripts = {"classpath:/sql/delete-workplace.sql", "classpath:/sql/delete-floor-plan.sql", "classpath:/sql/delete-office.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    @DisplayName("test on add one workplace")
    void postUser() throws Exception {

        Workplace workplace = workplaceRepository.findByWorkplaceNumber(1)
                .orElseThrow(() -> new NotFoundException("There is no workplace", Workplace.class, "workplaceNumber"));

        UserDto userDto = new UserDto();
        userDto.setFirstName("Usmon");
        userDto.setUsername("usmon");
        userDto.setPassword("usmon");
        userDto.setLastName("Usmon");
        userDto.setEmail("usmon@gmail.com");
        userDto.setTelegramId("12345");
        userDto.setPreferredWorkplace(workplace.getId());
        userDto.setRole("ADMIN");

        String jsonUser = objectMapper.writeValueAsString(userDto);

        MockHttpServletRequestBuilder requestBuilder = post("/user/add")
                .content(jsonUser)
                .header(HttpHeaders.AUTHORIZATION,
                        "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0b2toaXIiLCJhdXRob3JpdGllcyI6W3siYXV0aG9yaXR5IjoiUk9MRV9BRE1JTiJ9XSwiaWF0IjoxNjU3MDE0MzQ3LCJleHAiOjE3NTgxODAwMDB9.4B-7iVYsZzCpshSoMGJFg9Zjao26Lqjz8hJVKaCueU8")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder);

        Optional<User> optionalUser = userRepository.findByUsername(userDto.getUsername());
        Assertions.assertEquals(optionalUser.get().getUsername(), userDto.getUsername());
    }

    @Sql(scripts = {"classpath:/sql/insert-office.sql", "classpath:/sql/insert-sample-floor-plan.sql", "classpath:/sql/insert-workplace.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    void getUserById() throws Exception {

        Optional<Workplace> byId = workplaceRepository.findById(UUID.fromString("11111111-0000-0000-0000-111111111111"));

        User user = new User();
        user.setFirstName("Usmon");
        user.setUsername("usmon");
        user.setPassword("usmonjonbro");
        user.setLastName("Usmon");
        user.setEmail("usmon@gmail.com");
        user.setTelegramId("12345");
        user.setEmploymentEnd(LocalDate.now());
        user.setEmploymentStart(LocalDate.now());
        user.setUserState(UserState.MAIN_MENU);
        user.setPreferredWorkplace(byId.get());
        user.setRole(UserRole.ADMIN);

        User save = userRepository.save(user);

        mockMvc
                .perform(
                        get("/user/{id}", save.getId())
                                .header(HttpHeaders.AUTHORIZATION, "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0b2toaXIiLCJhdXRob3JpdGllcyI6W3siYXV0aG9yaXR5IjoiUk9MRV9BRE1JTiJ9XSwiaWF0IjoxNjU3MDE0MzQ3LCJleHAiOjE3NTgxODAwMDB9.4B-7iVYsZzCpshSoMGJFg9Zjao26Lqjz8hJVKaCueU8")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id", is(save.getId().toString())));
    }
}

