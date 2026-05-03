package com.vinay.fortexaBackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDTO {

    @NotBlank(message = "Username is Required")
    private String username;

    @NotBlank(message = "Password is Required")
    @Size(min = 6, message = "Password must be at least 6 character")
    private String password;

    private Boolean rememberMe;

}
