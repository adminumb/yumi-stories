package com.yumistories.service;

import com.yumistories.model.Story;
import com.yumistories.repository.StoryRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;



@Service
public class StoryService {


    private final StoryRepository storyRepository;
    private final Path uploadDir;

    // Значение "app.upload-dir" берётся из application.properties.
    // Так путь к папке не "зашит" в код и его легко поменять.
    public StoryService(StoryRepository storyRepository,
                        @Value("${app.upload-dir}") String uploadDir) {
        this.storyRepository = storyRepository;
        this.uploadDir = Path.of(uploadDir);
    }

    public List<Story> getAllStories() {
        return storyRepository.findAll();
    }

    public Story createStory(String title, String idea) {
        Story story = new Story(title, idea);
        return storyRepository.save(story);
    }

    public Story addPhoto(Long storyId, MultipartFile file) throws IOException {
        Story story = storyRepository.findById(storyId)
                .orElseThrow(() -> new NoSuchElementException("Story not found: " + storyId));

        Files.createDirectories(uploadDir);

        // Генерируем уникальное имя файла, чтобы фото с одинаковыми
        // названиями от разных пользователей/загрузок не перезаписывали друг друга.
        String extension = extractExtension(file.getOriginalFilename());
        String storedFilename = UUID.randomUUID() + extension;

        Path targetPath = uploadDir.resolve(storedFilename);
        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        story.addPhotoFilename(storedFilename);
        return storyRepository.save(story);
    }

    private String extractExtension(String originalFilename) {
        if (originalFilename == null || !originalFilename.contains(".")) {
            return "";
        }
        return originalFilename.substring(originalFilename.lastIndexOf("."));
    }
}

