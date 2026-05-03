package com.vinay.fortexaBackend.service;

import com.vinay.fortexaBackend.dto.SignupRequestDTO;
import com.vinay.fortexaBackend.entity.User;
import com.vinay.fortexaBackend.repository.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private final JWTService jwtService;
    private final UserRepo userRepo;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public String signupUser(SignupRequestDTO signupRequestDTO){

        if(signupRequestDTO == null){
            throw new RuntimeException("Invalid signup request");
        }

        Optional<User> exist = userRepo.findByUsername(signupRequestDTO.getUsername());
        if(exist.isPresent()){
            throw new RuntimeException("User already exist");
        }

        User user = new User();
        System.out.println(signupRequestDTO.getUsername());
        System.out.println(signupRequestDTO.getPassword());

        user.setUsername(signupRequestDTO.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(signupRequestDTO.getPassword()));

        userRepo.save(user);
        return jwtService.generateToken(signupRequestDTO.getUsername(), signupRequestDTO.getRemember());
    }
}
