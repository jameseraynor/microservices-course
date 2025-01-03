package com.apicourse.photapp.api.users.photo_api_users.shared;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import feign.Response;
import feign.codec.ErrorDecoder;

@ControllerAdvice
public class FeignErrorDecoder implements ErrorDecoder{

    @Override
    public Exception decode(String methodKey, Response response) {
        
        switch(response.status()) {
            case 400:
                // Do something
                break;
            case 404:
                if(methodKey.contains("getAlbums")) {
                    return new ResponseStatusException(HttpStatus.valueOf(response.status()),"Albums are not found");
                }
                break;
            default:
                return new Exception(response.reason());
        }
        return null;
    }

}
