package com.company.carpooling.services.contracts;

import com.company.carpooling.models.UserProfilePic;
import org.springframework.web.multipart.MultipartFile;

public interface UserProfilePicService {
    String uploadPictureToCloudinary(MultipartFile multipartFile);
}
