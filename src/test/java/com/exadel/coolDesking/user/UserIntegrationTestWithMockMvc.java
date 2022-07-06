package com.exadel.coolDesking.user;

import com.exadel.coolDesking.common.exception.NotFoundException;
import com.exadel.coolDesking.workspace.Workplace;
import com.exadel.coolDesking.workspace.WorkplaceRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application.properties")
@Transactional
@ExtendWith(SpringExtension.class)
public class UserIntegrationTestWithMockMvc {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WorkplaceRepository workplaceRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void postUser() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setFirstName("Usmon");
        userDto.setUsername("usmon");
        userDto.setPassword("usmon");
        userDto.setLastName("Usmon");
        userDto.setEmail("usmon@gmail.com");
        userDto.setTelegramId("12345");
        userDto.setPreferredWorkplace(UUID.fromString("be35aac2-19a8-4cbe-9d90-21ee6ec99406"));
        userDto.setRole("ADMIN");

        String jsonUser = objectMapper.writeValueAsString(userDto);

        MockHttpServletRequestBuilder requestBuilder = post("/user/add")
                .content(jsonUser)
                .header(HttpHeaders.AUTHORIZATION,
                        "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0b2toaXIiLCJhdXRob3JpdGllcyI6W3siYXV0aG9yaXR5IjoiUk9MRV9BRE1JTiJ9XSwiaWF0IjoxNjU3MDE0MzQ3LCJleHAiOjE3NTgxODAwMDB9.4B-7iVYsZzCpshSoMGJFg9Zjao26Lqjz8hJVKaCueU8")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());

        Optional<User> optionalUser = userRepository.findByUsername(userDto.getUsername());
        Assertions.assertEquals(optionalUser.get().getUsername(), userDto.getUsername());

    }

    @Test
    void getUserById() throws Exception {

        Optional<Workplace> byId = workplaceRepository.findById(UUID.fromString("be35aac2-19a8-4cbe-9d90-21ee6ec99406"));

        User user = new User();
        user.setId(UUID.fromString("96a74cea-a477-48f0-a703-665e4af61bf1"));
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
