package com.dsi.oauth2.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = {"/","index"}, produces = MediaType.APPLICATION_JSON_VALUE)
public class IndexController {

    @GetMapping("/")
    public String hello(){
        return "IndexIt works";
    }
}
