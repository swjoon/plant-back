package com.project.plantcare.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.project.plantcare.jwt.JwtAccessDeniedHandler;
import com.project.plantcare.jwt.JwtAuthenticationEntryPoint;
import com.project.plantcare.jwt.JwtRequestFilter;
import com.project.plantcare.jwt.JwtTokenProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Log4j2
public class SecurityConfig {

	private final JwtTokenProvider jwtTokenProvider;
	private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http.csrf((auth) -> auth.disable());
		http.exceptionHandling(a -> a.authenticationEntryPoint(jwtAuthenticationEntryPoint)
				.accessDeniedHandler(jwtAccessDeniedHandler));
		http.sessionManagement(a -> a.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		http.authorizeHttpRequests((auth) -> auth
				.requestMatchers("/", "/login", "/api/v1/user/login", "/api/v1/user/logout", "/api/v1/user/findpw","/api/v1/user/register","/api/v1/sensor/**")
				.permitAll().anyRequest().authenticated());
		
		// jwt필터를 usernamepassword인증 전에 실행
		http.addFilterBefore(new JwtRequestFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

		log.info("securityConfig");
		return http.build();
	}

}
