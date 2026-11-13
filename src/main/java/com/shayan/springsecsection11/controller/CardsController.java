package com.shayan.springsecsection11.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CardsController {

    @GetMapping("/myCards")
    public String getCardsDetail(){
        return "Here are the Cards details from the DB";
    }


}
