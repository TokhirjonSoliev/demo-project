package com.exadel.coolDesking.user;

import com.exadel.coolDesking.config.auth.ApplicationUser;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserRepositoryService implements UserDao{

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserRepositoryService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<ApplicationUser> selectApplicationUserByUsername(String username) {
        return getApplicationUsers()
                .stream()
                .filter(applicationUser -> username.equals(applicationUser.getUsername()))
                .findFirst();
    }

    private List<ApplicationUser> getApplicationUsers(){
        List<ApplicationUser> applicationUsers = new ArrayList<>();
        userRepository.findAll()
                .forEach(user -> {
                    ApplicationUser applicationUser = new ApplicationUser(
                            user.getUsername(),
                            user.getPassword(),
                            Set.of(new SimpleGrantedAuthority("ROLE_"+user.getRole().toString())),
                            true,
                            true,
                            true,
                            true
                    );
                    applicationUsers.add(applicationUser);
                });

        return applicationUsers;
    }
}
