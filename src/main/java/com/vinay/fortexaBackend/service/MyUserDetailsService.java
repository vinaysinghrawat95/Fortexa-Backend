package com.vinay.fortexaBackend.service;

import com.vinay.fortexaBackend.entity.UserPrincipal;
import com.vinay.fortexaBackend.repository.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username).map(UserPrincipal::new)
                .orElseThrow(()-> new UsernameNotFoundException("User not found"));
    }
}
