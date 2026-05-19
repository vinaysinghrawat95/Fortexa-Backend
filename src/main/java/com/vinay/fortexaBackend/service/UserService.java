package com.vinay.fortexaBackend.service;

import com.vinay.fortexaBackend.dto.LoginDTO;
import com.vinay.fortexaBackend.dto.SignupRequestDTO;
import com.vinay.fortexaBackend.entity.Role;
import com.vinay.fortexaBackend.entity.User;
import com.vinay.fortexaBackend.entity.UserPrincipal;
import com.vinay.fortexaBackend.repository.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private final JWTService jwtService;
    private final UserRepo userRepo;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private final Authentication authentication;

    public String signupUser(SignupRequestDTO signupRequestDTO){

        if(signupRequestDTO == null){
            throw new RuntimeException("Invalid signup request");
        }

        String username = signupRequestDTO.getUsername();

        if(username == null){
            throw new RuntimeException("Username is required");
        }

        username = username.trim().toLowerCase();

        Optional<User> exist = userRepo.findByUsername(username);

        if(exist.isPresent()){
            throw new RuntimeException("User already exist");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(bCryptPasswordEncoder.encode(signupRequestDTO.getPassword()));
        user.setRole(Role.ROLE_ADMIN);

        userRepo.save(user);
        return jwtService.generateToken(username, signupRequestDTO.getRememberMe(), Role.ROLE_USER);
    }

    public String loginUser(LoginDTO loginDTO) {
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginDTO.getUsername(),
                    loginDTO.getPassword()
                    )
            );

            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            return jwtService.generateToken(loginDTO.getUsername(), loginDTO.getRememberMe(), userPrincipal.getUser().getRole());
        }catch(BadCredentialsException ex){
            throw new RuntimeException("Invalid credential");
        }
    }
}
