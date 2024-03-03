package com.company.carpooling.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public class UserProfilePicServiceImpl implements  UserProfilePicService{
    private final String CLOUDINARY_URL="cloudinary://863521527225825:m9l6q3maftGM0QyYtDHbA7XaXFo@dmkprwzyp";
    @Override
    public String uploadPictureToCloudinary(MultipartFile multipartFile) {
        Cloudinary cloudinary = new Cloudinary(CLOUDINARY_URL);
        cloudinary.config.secure = true;
        try {
            Map params1 = ObjectUtils.asMap(
                    "use_filename", true,
                    "unique_filename", false,
                    "overwrite", true
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
}
