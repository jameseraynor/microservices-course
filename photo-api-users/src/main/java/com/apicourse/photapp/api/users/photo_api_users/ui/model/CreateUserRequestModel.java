package com.apicourse.photapp.api.users.photo_api_users.ui.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CreateUserRequestModel {

    @NotBlank(message = "First name cannot be blank")
    @Size(min=2, message = "First name must be at least 2 characters long")
    private String firstName;

    @NotBlank(message = "Last name cannot be blank")
    @Size(min=2, message = "Last name must be at least 2 characters long")
    private String lastName;

    @Email
    @NotBlank(message = "Email cannot be blank")
    private String email;
    
    @NotBlank(message = "Password cannot be blank")
    @Size(min=8, max=16, message = "Password must be equal or greater than 8 characters and less than 16 characters")
    private String password;

}
