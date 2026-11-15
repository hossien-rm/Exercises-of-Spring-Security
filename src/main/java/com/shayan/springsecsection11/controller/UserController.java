package com.shayan.springsecsection11.controller;

import com.shayan.springsecsection11.constant.ApplicationConstant;
import com.shayan.springsecsection11.dto.LoginRequestDTO;
import com.shayan.springsecsection11.dto.LoginResponseDTO;
import com.shayan.springsecsection11.entity.Customer;
import com.shayan.springsecsection11.repository.CustomerRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final Environment env;


    @PostMapping("/apiLogin")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequest) {
        String jwt = "";
        Authentication authentication = UsernamePasswordAuthenticationToken
                .unauthenticated(loginRequest.username(), loginRequest.password());
        Authentication authenticationResponse = authenticationManager.authenticate(authentication);
        if (authenticationResponse.isAuthenticated()) {
            String secret = env.getProperty(ApplicationConstant.JWT_SECRET_KEY
                    , ApplicationConstant.JWT_SECRET_DEFAULT_VALUE);
            SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
            jwt = Jwts.builder().setIssuer("Shayan Company").setSubject("JWT")
                    .claim("username", authenticationResponse.getName())
                    .claim("authorities", authenticationResponse.getAuthorities()
                            .stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.joining(", ")))
                    .setIssuedAt(new Date())
                    .setExpiration(new Date((new Date()).getTime() + 3000000))
                    .signWith(secretKey).compact();
        }
        return ResponseEntity.status(HttpStatus.CREATED).header(ApplicationConstant.JWT_HEADER, jwt)
                .body(new LoginResponseDTO(HttpStatus.OK.getReasonPhrase(), jwt));
    }


    @GetMapping("/user")
    public Iterable<Customer> findAll() {
        return customerRepository.findAll();
    }


    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Customer customer) {
        try {
            String hashPwd = passwordEncoder.encode(customer.getPwd());
            customer.setPwd(hashPwd);
            Customer saveCustomer = customerRepository.save(customer);
            if (customer.getId() > 0) {
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body("Given user details are successfully registered.");
            }
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An exception occurred: " + ex.getMessage());
        }
        return null;
    }

}
