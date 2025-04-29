package com.nefarious.socialnetwork.user.util;

import org.springframework.security.core.context.SecurityContextHolder;
import java.util.UUID;

public class CurrentUser {
    /** Gets the Principal User ID
     * @return Current Users userId UUID
     */
    public static UUID getPrincipal() {
        return (UUID) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
    }
}
