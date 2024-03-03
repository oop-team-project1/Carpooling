package com.company.carpooling.services;

import org.springframework.web.multipart.MultipartFile;

public interface UserProfilePicService {
    String uploadPictureToCloudinary(MultipartFile multipartFile);
}
