package com.yumistories.controller;

import com.yumistories.model.Scene;
import com.yumistories.service.SceneService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.NoSuchElementException;

// Вложенный путь /api/stories/{storyId}/scenes подчёркивает, что сцена
// не существует сама по себе — она всегда принадлежит конкретной истории.
@RestController
@RequestMapping("/api/stories/{storyId}/scenes")
public class SceneController {

    private final SceneService sceneService;

    public SceneController(SceneService sceneService) {
        this.sceneService = sceneService;
    }

    @GetMapping
    public List<Scene> getScenes(@PathVariable Long storyId) {
        return sceneService.getScenesForStory(storyId);
    }

    @PostMapping
    public ResponseEntity<Scene> addScene(@PathVariable Long storyId,
                                          @RequestBody AddSceneRequest request) {
        try {
            Scene scene = sceneService.addScene(storyId, request.text());
            return ResponseEntity.ok(scene);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    public record AddSceneRequest(String text) {
    }
}