package ru.hogwarts.school.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.dto.AvatarDtoOut;
import ru.hogwarts.school.entity.Avatar;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.exception.AvatarNotFoundException;
import ru.hogwarts.school.exception.AvatarProcessingException;
import ru.hogwarts.school.mapper.AvatarMapper;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.service.AvatarService;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class AvatarServiceImpl implements AvatarService {

    private final AvatarRepository avatarRepository;
    private final AvatarMapper avatarMapper;
    private final Path pathToAvatarsDir;

    @Autowired
    public AvatarServiceImpl(final AvatarRepository avatarRepository,
                             final AvatarMapper avatarMapper,
                             @Value("${path.to.avatars.folder}") String pathToAvatarsDir
    ) {
        this.avatarRepository = avatarRepository;
        this.avatarMapper = avatarMapper;
        this.pathToAvatarsDir = Path.of(pathToAvatarsDir);
    }

    @Override
    public Avatar findAvatar(Long id) {
        return avatarRepository.findByStudentId(id).orElseGet(Avatar::new);
    }

    @Override
    public Avatar create(Student student, MultipartFile file) {
        try {
            String contentType = file.getContentType();
            String extension = getExtensions(Objects.requireNonNull(file.getOriginalFilename()));
            byte[] data = file.getBytes();
            String fileName = UUID.randomUUID() + "." + extension;
            Path pathToAvatar = pathToAvatarsDir.resolve(fileName);
            writeToFile(pathToAvatar, data);
//            Files.write(pathToAvatar, data);

            Avatar avatar = findAvatar(student.getId());

            if (avatar.getFilePath() != null) {
                Files.delete(Path.of(avatar.getFilePath()));
            }

            avatar.setMediaType(contentType);
            avatar.setFileSize(data.length);
            avatar.setData(data);
            avatar.setStudent(student);
            avatar.setFilePath(pathToAvatar.toString());
            return avatarRepository.save(avatar);
        } catch (IOException e) {
            throw new AvatarProcessingException();
        }
    }

    private void writeToFile(Path path, byte[] data) {
        try (FileOutputStream fos = new FileOutputStream(path.toFile())) {
            fos.write(data);
        } catch (IOException e) {
            throw new AvatarProcessingException();
        }
    }

    private byte[] read(Path path) {
        try (FileInputStream fis = new FileInputStream(path.toFile())) {
            return fis.readAllBytes();
        } catch (IOException e) {
            throw new AvatarProcessingException();
        }
    }

    @Override
    public Pair<byte[], String> getFromDB(Long id) {
        Avatar avatar = avatarRepository.findById(id)
                .orElseThrow(() -> new AvatarNotFoundException(id));
        return Pair.of(avatar.getData(), avatar.getMediaType());
    }

    @Override
    public Pair<byte[], String> getFromFS(Long id) {
        Avatar avatar = avatarRepository.findById(id)
                .orElseThrow(() -> new AvatarNotFoundException(id));
        return Pair.of(read(Path.of(avatar.getFilePath())), avatar.getMediaType());

        /*try {
            Avatar avatar = avatarRepository.findById(id)
                    .orElseThrow(() -> new AvatarNotFoundException(id));
            return Pair.of(Files.readAllBytes(Path.of(avatar.getFilePath())), avatar.getMediaType());
        } catch (IOException e) {
            throw new AvatarProcessingException();
        }*/
    }

    @Override
    public List<AvatarDtoOut> getAllAvatars(final Integer pageNumber, final Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize);
        return avatarRepository.findAll(pageRequest).stream()
                .map(avatarMapper::toDto)
                .toList();
    }

    private String getExtensions(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

}
