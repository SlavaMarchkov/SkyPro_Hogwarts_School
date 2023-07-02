package ru.hogwarts.school.service;

import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.entity.Avatar;

import java.io.IOException;

public interface AvatarService {

    void uploadAvatar(Long id, MultipartFile file) throws IOException;

    Avatar findAvatar(Long id);

}
