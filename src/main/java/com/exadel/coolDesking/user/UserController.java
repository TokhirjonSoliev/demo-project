package com.exadel.coolDesking.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/user")
public class UserController {

    private final UserService userService;

    @GetMapping(path = "/{id}")
    public User getById(@PathVariable UUID id) {
        return null;
    }

    @PostMapping
    public User create(@RequestBody @Valid User user) {
        return null;
    }
}
