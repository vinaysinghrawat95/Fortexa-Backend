package com.vinay.fortexaBackend.controller;

import com.vinay.fortexaBackend.dto.SignupRequestDTO;
import com.vinay.fortexaBackend.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class UserController {

    private final UserService userService;

    @GetMapping("/greet")
    public String greet(){
        return "Welcome to the Fortexa!";
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signupUser(@Valid @RequestBody SignupRequestDTO signupRequestDTO){
        return new ResponseEntity<>(userService.signupUser(signupRequestDTO), HttpStatus.CREATED);
    }

}
