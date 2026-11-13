package com.shayan.springsecsection11.config;


import com.shayan.springsecsection11.exception.CustomAccessDenied;
import com.shayan.springsecsection11.exception.CustomBasicAuthenticationEntryPoint;
import com.shayan.springsecsection11.filter.*;
import jakarta.servlet.http.HttpServletRequest;
import org.jspecify.annotations.Nullable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;
import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@Profile("!prod")
public class ProjectSecurityConfig {

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

        CsrfTokenRequestAttributeHandler csrfTokenRequestAttributeHandler = new CsrfTokenRequestAttributeHandler();


        http

                .sessionManagement(sessConfig -> sessConfig
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .cors(corsConfig -> corsConfig.configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(@Nullable HttpServletRequest request) {
                        CorsConfiguration config = new CorsConfiguration();
                        config.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
                        config.setAllowedMethods(Collections.singletonList("*"));
                        config.setAllowCredentials(true);
                        config.setAllowedHeaders(Collections.singletonList("*"));
                        config.setExposedHeaders(List.of("JWTHeader"));
                        config.setMaxAge(3600L);
                        return config;
                    }
                }))

                .csrf(csrfConfig -> csrfConfig
                        .csrfTokenRequestHandler(csrfTokenRequestAttributeHandler)
                        .ignoringRequestMatchers("/myCards", "/register") //working those endpoints without csrf token
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))//generate csrf token

                .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
                .addFilterBefore(new RequestValidationBeforeFilter(), BasicAuthenticationFilter.class)
                .addFilterAfter(new AuthoritiesLoggingAfterFilter(), BasicAuthenticationFilter.class)
                .addFilterAt(new AuthoritiesLoggingAtFilter(), BasicAuthenticationFilter.class)
                .addFilterAfter(new JWTTokenGeneratorFilter(), BasicAuthenticationFilter.class)
                .addFilterBefore(new JWTTokenValidatorFilter(), BasicAuthenticationFilter.class);

        http
                .authorizeHttpRequests((request) -> request
                        /*.requestMatchers("/myAccount").hasAuthority("ACCOUNTVIEW")
                        .requestMatchers("/myBalance").hasAuthority("BALANCEVIEW")
                        .requestMatchers("/myLoan").hasAuthority("LOANVIEW")*/
                        .requestMatchers("/myAccount").hasAuthority("USER")
                        .requestMatchers("/myBalance").hasAuthority("USER")
                        .requestMatchers("/myLoan").hasAuthority("ADMIN")
                        .requestMatchers("/user").hasAuthority("ADMIN")
                        .requestMatchers(
                                "/myContact"
                                , "/myCards"
                                , "/error"
                                , "/register"
                                , "/authority/*"
                                , "/Invalidsession").permitAll());

        http.formLogin(withDefaults()); //flc->flc.disable() for disable log page

        http.httpBasic(exception -> exception
                .authenticationEntryPoint(new CustomBasicAuthenticationEntryPoint()));
        //handle global exceptions
        http.exceptionHandling(
                exception -> exception
                        .accessDeniedHandler(new CustomAccessDenied()));
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    //cause check your password is complex or not if not you see Invalid.
    // since java 6.3 is active
    @Bean
    public CompromisedPasswordChecker compromisedPasswordChecker() {
        return new HaveIBeenPwnedRestApiPasswordChecker();
    }


}
