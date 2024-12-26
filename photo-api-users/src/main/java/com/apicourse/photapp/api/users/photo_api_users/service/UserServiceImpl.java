package com.apicourse.photapp.api.users.photo_api_users.service;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.apicourse.photapp.api.users.photo_api_users.data.UserEntity;
import com.apicourse.photapp.api.users.photo_api_users.data.UsersRepository;
import com.apicourse.photapp.api.users.photo_api_users.shared.UserDto;
import com.apicourse.photapp.api.users.photo_api_users.ui.model.AlbumResponseModel;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private UsersRepository usersRepository;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private RestTemplate restTemplate;

    private Environment environment;

    @Autowired
    public UserServiceImpl(UsersRepository usersRepository, BCryptPasswordEncoder bCryptPasswordEncoder,
            RestTemplate restTemplate, Environment environment) {
        this.usersRepository = usersRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.restTemplate = restTemplate;
        this.environment = environment;
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

    @Override
    public UserDto getUserByUserId(String userId) {

        UserEntity entity = usersRepository.findByUserId(userId);

        if (entity == null)
            throw new UsernameNotFoundException("User not found");

        System.out.println("in api users "+entity.toString());
        
        UserDto dto = new ModelMapper().map(entity, UserDto.class);
        String albumsUrl = String.format(environment.getProperty("albums.url") , userId);

        ResponseEntity<List<AlbumResponseModel>> albumsList = restTemplate.exchange(
                albumsUrl, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<AlbumResponseModel>>() {
                });
        List<AlbumResponseModel> albums = albumsList.getBody();

        dto.setAlbums(albums);

        return dto;
    }

}
