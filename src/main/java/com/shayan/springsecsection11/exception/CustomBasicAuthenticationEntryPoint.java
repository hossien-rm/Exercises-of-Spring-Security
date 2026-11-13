package com.shayan.springsecsection11.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.time.LocalDateTime;

public class CustomBasicAuthenticationEntryPoint implements AuthenticationEntryPoint {


    @Override
    public void commence(@NonNull HttpServletRequest request
            , @NonNull HttpServletResponse response
            , @NonNull AuthenticationException authException) throws IOException, ServletException {

        LocalDateTime currentDateTime = LocalDateTime.now();
        String message = ( authException.getMessage() != null) ? authException.getMessage():
                "UnAuthorized";
        String path=request.getRequestURI();

                response.setHeader("shayan-error-handle", "Authentication failed");
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType("application/json;charset=UTF-8");

        String jsonResponse =
                String.format("{\"timestamp\":\"%s\",\"status\":\"%d\",\"error\":\"%s\",\"message\":\"%s\",\"path\":\"%s\"}"
                        , currentDateTime, HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                        message, path);

        response.getWriter().write(jsonResponse);
    }
}
