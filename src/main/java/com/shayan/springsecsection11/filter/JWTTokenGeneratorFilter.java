package com.shayan.springsecsection11.filter;

import com.shayan.springsecsection11.constant.ApplicationConstant;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.stream.Collectors;

public class JWTTokenGeneratorFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request
            , @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Environment env = getEnvironment();
            String secret = env.getProperty(ApplicationConstant.JWT_SECRET_KEY
                    , ApplicationConstant.JWT_SECRET_DEFAULT_VALUE);
            SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
            String jwt = Jwts.builder().setIssuer("Shayan company").setSubject("jwt token")
                    .claim("username", authentication.getName())
                    .claim("authorities", authentication.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.joining(", ")))
                    .setIssuedAt(new Date())
                    .setExpiration(new Date((new Date()).getTime() + 3000000))
                    .signWith(secretKey)
                    .compact();
            response.setHeader(ApplicationConstant.JWT_HEADER, jwt);

        }
        filterChain.doFilter(request, response);
    }

    // cause do this filter when call /user
    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) throws ServletException {
        return !request.getServletPath().equals("/user");
    }
}
