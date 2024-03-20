package com.company.carpooling.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.company.carpooling.models.UserProfilePic;
import com.company.carpooling.repositories.UserProfilePicRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@AllArgsConstructor
@Service
public class UserProfilePicServiceImpl implements UserProfilePicService {
    private final String CLOUDINARY_URL = "cloudinary://863521527225825:m9l6q3maftGM0QyYtDHbA7XaXFo@dmkprwzyp";
    private final UserProfilePicRepository userProfilePicRepository;

    @Override
    public String uploadPictureToCloudinary(MultipartFile multipartFile) {
        Cloudinary cloudinary = new Cloudinary(CLOUDINARY_URL);
        cloudinary.config.secure = true;
        try {
            Map params1 = ObjectUtils.asMap(
                    "use_filename", true,
                    "unique_filename", true,
                    "overwrite", false
            );
            return cloudinary
                    .uploader()
                    .upload(multipartFile.getBytes(), params1)
                    .get("secure_url")
                    .toString();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public UserProfilePic getById(int id) {
        return userProfilePicRepository.getById(id);
    }
}
