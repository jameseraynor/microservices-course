package com.apicourse.photapp.api.users.photo_api_users.data;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.apicourse.photapp.api.users.photo_api_users.ui.model.AlbumResponseModel;

@FeignClient(name = "albums-ws")
public interface AlbumServiceClient {

    @GetMapping("/users/{userId}/albumss")
    public List<AlbumResponseModel> getAlbums(@PathVariable String userId);

}
