package com.shayan.springsecsection11.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ContactController {

    @GetMapping("/myContact")
    public String getContactDetail(){
        return "Here are the Contacts details from the DB";
    }

}
