package com.apicourse.photapp.api.users.photo_api_users.data;

import java.util.ArrayList;
import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.apicourse.photapp.api.users.photo_api_users.ui.model.AlbumResponseModel;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

@FeignClient(name = "albums-ws")
public interface AlbumServiceClient {

    @GetMapping("/users/{userId}/albums")
    @Retry(name = "albums-ws")
    @CircuitBreaker(name = "albums-ws", fallbackMethod = "getAlbumsFallback")
    public List<AlbumResponseModel> getAlbums(@PathVariable String userId);

    public default List<AlbumResponseModel> getAlbumsFallback(String userId, Exception e) {
        System.out.println("Fallback method called");
        System.out.println("userId: " + userId);
        System.out.println("Exception: " + e.getMessage());
        return new ArrayList<>();
    }

}
