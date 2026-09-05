package com.yumistories.controller;

import com.yumistories.model.Story;
import com.yumistories.service.StoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/stories")
public class StoryController {

    private final StoryService storyService;

    public StoryController(StoryService storyService) {
        this.storyService = storyService;
    }

    @GetMapping
    public List<Story> getAllStories() {
        return storyService.getAllStories();
    }

    @PostMapping
    public Story createStory(@RequestBody CreateStoryRequest request) {
        return storyService.createStory(request.title(), request.idea());
    }

    // "consumes = multipart/form-data" — этот эндпоинт принимает не JSON,
    // а файл, отправленный через FormData на фронте.
    @PostMapping(value = "/{id}/photos", consumes = "multipart/form-data")
    public ResponseEntity<Story> addPhoto(@PathVariable Long id,
                                          @RequestParam("file") MultipartFile file) {
        try {
            Story updated = storyService.addPhoto(id, file);
            return ResponseEntity.ok(updated);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public record CreateStoryRequest(String title, String idea) {
    }
}