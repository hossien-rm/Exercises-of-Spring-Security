package com.shayan.springsecsection11.controller;

import com.shayan.springsecsection11.entity.Authority;
import com.shayan.springsecsection11.entity.Customer;
import com.shayan.springsecsection11.repository.AuthorityRepository;
import com.shayan.springsecsection11.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthorityController {

    private final AuthorityRepository authorityRepository;
    private final CustomerRepository customerRepository;

    @PostMapping("/authority/{customerId}")
    public ResponseEntity<String> createAuthority(@RequestBody Authority authority, @PathVariable Long customerId){

        Customer customer=customerRepository.findById(customerId)
                .orElseThrow(()-> new RuntimeException("customer id not found"));
        authority.setCustomer(customer);
        Authority saveAuthority=authorityRepository.save(authority);

        if(saveAuthority.getId()>0){
            return ResponseEntity.status(HttpStatus.CREATED.value())
                    .body("Your role add is : "+ saveAuthority.getName());
        }else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .body("I can not create authority");
        }
    }
}
