package com.yumistories.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Первый и единственный эндпоинт итерации 1.
 * Его единственная задача — доказать, что backend жив и отвечает JSON-ом,
 * а frontend умеет этот JSON получить и показать.
 */
@RestController
public class PingController {

    @GetMapping("/api/ping")
    public Map<String, String> ping() {
        return Map.of("status", "ok", "message", "Yumi Stories backend is alive");
    }

}
