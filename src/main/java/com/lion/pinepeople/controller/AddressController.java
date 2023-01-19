package com.lion.pinepeople.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AddressController {

        @GetMapping("/")
        public String home(){
            return "map/homeMap";
        }

}