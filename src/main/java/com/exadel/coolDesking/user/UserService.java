package com.exadel.coolDesking.user;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    /*public User getByTelegramId(String telegramId) {
        return userRepository.findByTelegramId(telegramId)
                .orElseThrow(() -> new BotNotFoundException(
                        "User is not found",
                        User.class,
                        telegramId));
    }*/

    /*public void updateUserStatus(UserState userState, String telegramId) {
        int i = userRepository.setUserStatus(userState, telegramId);
        if (i == 0)
            throw new BotNotFoundException(
                    "UserState cannot be updated. User not found",
                    User.class,
                    telegramId);
    }*/

    public void deleteUserPreferredWorkplaceId(List<User> userList){
        for(User user: userList){
            user.setPreferredWorkplace(null);
            userRepository.save(user);
        }
    }
}
