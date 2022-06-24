package com.exadel.coolDesking.user;


import com.exadel.coolDesking.common.exception.ConflictException;
import com.exadel.coolDesking.common.exception.NotFoundException;
import com.exadel.coolDesking.workspace.Workplace;
import com.exadel.coolDesking.workspace.WorkplaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final WorkplaceRepository workplaceRepository;
    private final PasswordEncoder passwordEncoder;

    public User getUser(UUID userId){
        return userRepository.findById(userId)
                .orElseThrow(()-> new NotFoundException("There is no such user", User.class, "id"));
    }

    public User addUser(UserDto userDto){
        Workplace workplace = workplaceRepository.findById(userDto.getPreferredWorkplace())
                .orElseThrow(()-> new NotFoundException("There is no such workplace", Workplace.class, "workplaceId"));

        Optional<User> byUsername = userRepository.findByUsername(userDto.getUsername());
        if (byUsername.isPresent()){
            throw new ConflictException("This username is already taken", User.class, "username");
        }

        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setPreferredWorkplace(workplace);
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setUsername(userDto.getUsername());
        user.setTelegramId(userDto.getTelegramId());
        user.setRole(UserRole.valueOf(userDto.getRole()));
        user.setEmploymentStart(userDto.getEmploymentStart());
        user.setUserState(UserState.valueOf("MAIN_MENU"));

        User save = userRepository.save(user);
        return save;
    }
}
