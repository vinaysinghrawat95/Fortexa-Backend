package com.vinay.fortexaBackend.service;

import com.vinay.fortexaBackend.dto.LoginDTO;
import com.vinay.fortexaBackend.dto.SignupRequestDTO;
import com.vinay.fortexaBackend.entity.Role;
import com.vinay.fortexaBackend.entity.User;
import com.vinay.fortexaBackend.entity.UserPrincipal;
import com.vinay.fortexaBackend.exception.AppException;
import com.vinay.fortexaBackend.exception.ErrorCode;
import com.vinay.fortexaBackend.repository.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private final JWTService jwtService;
    private final UserRepo userRepo;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;

    private static final int MAX_ATTEMPTS = 5   ;
    private static final int LOCK_DURATION_MINUTES = 15;

    public String signupUser(SignupRequestDTO signupRequestDTO){

        if(signupRequestDTO == null){
            throw new AppException(ErrorCode.INVALID_SIGNUP_REQUEST);
        }

        String username = signupRequestDTO.getUsername();

        if(username == null || username.trim().isEmpty()){
            throw new AppException(ErrorCode.USERNAME_REQUIRED);
        }

        username = username.trim().toLowerCase();

        Optional<User> exist = userRepo.findByUsernameAndDeletedAtIsNull(username);

        if(exist.isPresent()){
            throw new AppException(ErrorCode.USER_ALREADY_EXISTS);
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(bCryptPasswordEncoder.encode(signupRequestDTO.getPassword()));
        user.setRole(Role.ROLE_USER);
        user.setEnabled(true);

        userRepo.save(user);
        return jwtService.generateToken(username, signupRequestDTO.getRememberMe(), Role.ROLE_USER);
    }

    public String loginUser(LoginDTO loginDTO) {

        User user = userRepo.findByUsernameAndDeletedAtIsNull(loginDTO.getUsername().trim().toLowerCase())
                .orElseThrow(()-> new AppException(ErrorCode.INVALID_CREDENTIALS));

        if(!user.isAccountNonLocked()){
            long minutesPassed = ChronoUnit.MINUTES.between(user.getLockTime(), LocalDateTime.now());
            if(minutesPassed >= LOCK_DURATION_MINUTES){
                user.setAccountNonLocked(true);
                user.setFailedAttempts(0);
                user.setLockTime(null);
                userRepo.save(user);
            }else {
                long remaining = LOCK_DURATION_MINUTES - minutesPassed;
                throw new AppException(ErrorCode.ACCOUNT_LOCKED,"Account locked, Try again after "+ remaining + " minutes");
            }
        }

        try{
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginDTO.getUsername().trim().toLowerCase(),
                    loginDTO.getPassword()
                    )
            );

            user.setFailedAttempts(0);
            userRepo.save(user);
            UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();
            return jwtService.generateToken(loginDTO.getUsername(), loginDTO.getRememberMe(), userPrincipal.getUser().getRole());

        }catch(BadCredentialsException ex){
            int attempts = user.getFailedAttempts() + 1;
            user.setFailedAttempts(attempts);

            if(attempts >= MAX_ATTEMPTS){
                user.setAccountNonLocked(false);
                user.setLockTime(LocalDateTime.now());
                userRepo.save(user);
                throw new AppException(ErrorCode.ACCOUNT_LOCKED,"Account locked due to 5 attempts, Try after 15 minutes");
            }

            userRepo.save(user);
            throw new AppException(ErrorCode.INVALID_CREDENTIALS,"Invalid credentials. " + (MAX_ATTEMPTS - attempts) + " attempts remaining");
        }
    }

    public void deleteUser(String username){
        User user = userRepo.findByUsernameAndDeletedAtIsNull(username)
                .orElseThrow(()-> new AppException(ErrorCode.USER_NOT_FOUND));

        user.setDeletedAt(LocalDateTime.now());
        userRepo.save(user);
    }
}
