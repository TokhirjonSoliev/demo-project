package com.exadel.coolDesking.auditing;

import com.exadel.coolDesking.user.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.UUID;

public class SpringSecurityAuditingAwareImpl implements AuditorAware<UUID> {
    @Override
    public Optional<UUID> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication!=null
                && authentication.isAuthenticated()
                && !authentication.getPrincipal().equals("anonymous")){

            User user = (User) authentication.getPrincipal();

            return Optional.ofNullable(user.getId());
        }
        return Optional.empty();
    }
}







