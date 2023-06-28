package ru.hogwarts.school.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.service.AvatarService;

import java.io.IOException;

@RestController
@RequestMapping(path = "/avatar")
public class AvatarController {

    private final AvatarService avatarService;

    @Autowired
    public AvatarController(final AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    @PostMapping(value = "{studentId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadAvatar(@PathVariable Long studentId,
                                               @RequestParam MultipartFile avatar
    ) throws IOException {
        if (avatar.getSize() >= 1024 * 300) {
            return ResponseEntity.badRequest().body("Avatar is too big");
        }
        avatarService.uploadAvatar(studentId, avatar);
        return ResponseEntity.ok().build();
    }

}
