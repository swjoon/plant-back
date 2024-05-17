package com.project.plantcare.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class SecurityUtil {

    private SecurityUtil() {}

    public static String getCurrentUsername() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        log.info("SecurityUtil: " + authentication);
        if (authentication == null || authentication.getName() == null) {
            throw new RuntimeException("인증 정보가 없습니다.");
        }

        log.info(authentication.getName());
        return authentication.getName();
    }
    
    public static String getCurrentUserRole() {
    	final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	log.info("SecurityUtil: " + authentication);
        if (authentication == null || authentication.getAuthorities() == null) {
            throw new RuntimeException("인증 정보가 없습니다.");
        }
        
        log.info(authentication.getAuthorities());
        return authentication.getAuthorities().toString();
    }
}