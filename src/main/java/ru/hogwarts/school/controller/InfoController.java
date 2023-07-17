package ru.hogwarts.school.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Контроллер по работе с портом")
public class InfoController {

    @Value("${server.port}")
    private int port;

    @GetMapping(path = "getPort")
    public int getPort() {
        return port;
    }

}
