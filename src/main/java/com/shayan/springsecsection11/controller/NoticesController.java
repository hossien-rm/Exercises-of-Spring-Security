package com.shayan.springsecsection11.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NoticesController {

    @GetMapping("/myNotice")
    public String welcome(){
        return "Here are the notice details from the DB";
    }

}
