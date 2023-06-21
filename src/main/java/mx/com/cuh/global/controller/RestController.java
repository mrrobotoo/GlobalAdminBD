package mx.com.cuh.global.controller;
import org.springframework.beans.factory.annotation.Autowired;

import mx.com.cuh.global.service.User;

@org.springframework.web.bind.annotation.RestController
public class RestController {
    @Autowired
    private User user;

}