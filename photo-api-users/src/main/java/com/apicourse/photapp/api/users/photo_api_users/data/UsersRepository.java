package com.apicourse.photapp.api.users.photo_api_users.data;

import org.springframework.data.repository.CrudRepository;

public interface UsersRepository extends CrudRepository<UserEntity, Long>{

    UserEntity findByEmail(String email);

    UserEntity findByUserId(String userId);
}
