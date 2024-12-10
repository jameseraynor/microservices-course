package com.apicourse.photapp.api.users.photo_api_users.shared;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDto implements Serializable{

    private static final long serialVersionUID = -2021526305171807990L;
    
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String encryptedPassword;
    private String userId;

}
