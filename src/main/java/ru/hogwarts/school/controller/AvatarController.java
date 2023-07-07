package ru.hogwarts.school.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.dto.AvatarDtoOut;
import ru.hogwarts.school.service.AvatarService;

import java.util.List;

@RestController
@RequestMapping(path = "/avatar")
@Tag(name = "Контроллер по работе с аватарами")
public class AvatarController {

    private final AvatarService avatarService;

    @Autowired
    public AvatarController(final AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    @GetMapping
    public List<AvatarDtoOut> getAllAvatars(@RequestParam(value = "page") Integer pageNumber,
                                            @RequestParam(value = "size") Integer pageSize
    ) {
        return avatarService.getAllAvatars(pageNumber, pageSize);
    }

    @GetMapping(value = "{id}/avatar-from-db")
    public ResponseEntity<byte[]> getAvatarFromDB(@PathVariable(value = "id") Long id) {
        return build(avatarService.getFromDB(id));
    }

    @GetMapping(value = "{id}/avatar-from-file")
    public ResponseEntity<byte[]> getAvatarFromFS(@PathVariable(value = "id") Long id) {
        return build(avatarService.getFromFS(id));
    }

    private ResponseEntity<byte[]> build(Pair<byte[], String> pair) {
        byte[] data = pair.getFirst();
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(pair.getSecond()))
                .contentLength(data.length)
                .body(data);
    }

}
