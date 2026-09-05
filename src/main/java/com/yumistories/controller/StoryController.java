package com.yumistories.controller;

import com.yumistories.model.Story;
import com.yumistories.repository.StoryRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Пока без отдельного service-слоя: логика тривиальна (просто сохранить
 * и просто вернуть список), поэтому контроллер работает с репозиторием
 * напрямую. Когда появится реальная логика (например, генерация сцен),
 * вынесем её в StoryService — не раньше, чем она появится.
 */
@RestController
@RequestMapping("/api/stories")
public class StoryController {

    private final StoryRepository storyRepository;

    // Spring сам создаст StoryRepository и подставит его сюда —
    // это называется "внедрение зависимостей через конструктор".
    public StoryController(StoryRepository storyRepository) {
        this.storyRepository = storyRepository;
    }

    @GetMapping
    public List<Story> getAllStories() {
        return storyRepository.findAll();
    }

    @PostMapping
    public Story createStory(@RequestBody CreateStoryRequest request) {
        Story story = new Story(request.title(), request.idea());
        return storyRepository.save(story);
    }

    /**
     * Отдельный класс для входящего JSON, а не сама сущность Story.
     * Так фронт не может случайно прислать "id" и переопределить его —
     * id всегда генерируется базой данных.
     */
    public record CreateStoryRequest(String title, String idea) {
    }
}
