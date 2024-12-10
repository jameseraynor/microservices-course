package com.apicourse.photapp.api.users.photo_api_users.service;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.apicourse.photapp.api.users.photo_api_users.data.UserEntity;
import com.apicourse.photapp.api.users.photo_api_users.data.UsersRepository;
import com.apicourse.photapp.api.users.photo_api_users.shared.UserDto;
import java.util.ArrayList;

@Service
public class UserServiceImpl implements UserService {

    private UsersRepository usersRepository;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UsersRepository usersRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.usersRepository = usersRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public UserDto createUser(UserDto userDetail) {

        userDetail.setUserId(UUID.randomUUID().toString());
        userDetail.setEncryptedPassword(bCryptPasswordEncoder.encode(userDetail.getPassword()));
        ModelMapper mapper = new ModelMapper();

        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserEntity entity = mapper.map(userDetail, UserEntity.class);
        usersRepository.save(entity);

        UserDto returnValue = mapper.map(entity, UserDto.class);

        return returnValue;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = usersRepository.findByEmail(username);

        if (userEntity == null)
            throw new UsernameNotFoundException(username);

        return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), true, true, true, true,
                new ArrayList<>());

    }

    @Override
    public UserDto getUserDetailsByEmail(String email) {
        UserEntity entity = usersRepository.findByEmail(email);

        if (entity == null)
            throw new UsernameNotFoundException(email);

        return new ModelMapper().map(entity, UserDto.class);
    }

}
