package com.apicourse.photapp.api.users.photo_api_users.ui.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserResponseModel {

    private String firstName;
    private String lastName;
    private String email;
    private String userId;

}
