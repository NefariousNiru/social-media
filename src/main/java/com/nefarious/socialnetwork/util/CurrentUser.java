package com.nefarious.socialnetwork.util;

import org.springframework.security.core.context.SecurityContextHolder;
import java.util.UUID;

public class CurrentUser {
    public static UUID getPrincipal() {
        return (UUID) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
    }
}
