package com.shayan.springsecsection11.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.time.LocalDateTime;

public class CustomAccessDenied implements AccessDeniedHandler {

    @Override
    public void handle(@NonNull  HttpServletRequest request
            ,@NonNull  HttpServletResponse response
            , @NonNull AccessDeniedException accessDeniedException) throws IOException, ServletException {

        LocalDateTime currentDateTime = LocalDateTime.now();
        String message = ( accessDeniedException.getMessage() != null) ? accessDeniedException.getMessage():
                "Authorization failed";
        String path=request.getRequestURI();

        response.setHeader("shayan-denied-handle", "Authorization failed");
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json;charset=UTF-8");

        String jsonResponse =
                String.format("{\"timestamp\":\"%s\",\"status\":\"%d\",\"error\":\"%s\",\"message\":\"%s\",\"path\":\"%s\"}"
                        , currentDateTime, HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                        message, path);

        response.getWriter().write(jsonResponse);
    }
}
