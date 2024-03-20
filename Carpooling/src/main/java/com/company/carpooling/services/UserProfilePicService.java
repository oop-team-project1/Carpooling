package com.company.carpooling.services;

import com.company.carpooling.models.UserProfilePic;
import org.springframework.web.multipart.MultipartFile;

public interface UserProfilePicService {
    String uploadPictureToCloudinary(MultipartFile multipartFile);
}
