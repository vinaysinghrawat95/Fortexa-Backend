package com.vinay.fortexaBackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SignupRequestDTO {

    @NotBlank(message = "Username is Required")
    @Size(min = 2, max = 20)
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9._]{2,19}$",
    message = "Username must start with a letter and contains only letters, numbers, . or _")
    private String username;

    @NotBlank(message = "Password is Required")
    @Size(min = 6, message = "Password must be at least 6 character")
    private String password;
    private  Boolean rememberMe;

}
