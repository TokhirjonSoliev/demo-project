package com.exadel.coolDesking.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/user")
public class UserController {

    private final UserService userService;

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        User user = userService.getUser(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/add")
    public ResponseEntity<?> create(@RequestBody @Valid UserDto userDto) {
        User user = userService.addUser(userDto);
        return ResponseEntity.ok(user);
    }
}
