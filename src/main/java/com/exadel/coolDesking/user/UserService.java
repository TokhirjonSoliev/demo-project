package com.exadel.coolDesking.user;


import com.exadel.coolDesking.common.exception.ConflictException;
import com.exadel.coolDesking.common.exception.NotFoundException;
import com.exadel.coolDesking.config.mapper.UserMapper;
import com.exadel.coolDesking.workspace.Workplace;
import com.exadel.coolDesking.workspace.WorkplaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final WorkplaceRepository workplaceRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

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


        User user = userMapper.userDtoToUser(userDto);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setPreferredWorkplace(workplace);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);
        user.setUserState(UserState.valueOf("MAIN_MENU"));
        user.setEmploymentEnd(userDto.getEmploymentEnd());

        User save = userRepository.save(user);
        return save;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException(String.format("Username %s not found", username)));

    }
}
