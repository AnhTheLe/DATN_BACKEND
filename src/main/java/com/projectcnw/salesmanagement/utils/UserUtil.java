package com.projectcnw.salesmanagement.utils;

import com.projectcnw.salesmanagement.models.Customer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserUtil {
    public static Integer getCurrentCustomerId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof Customer) {
            Customer user = (Customer) authentication.getPrincipal();
            Integer userId = user.getId();
            return userId;
        }
        return null; // hoặc return 0, return -1, vv.
    }
}
