package com.yumistories.service;

import com.yumistories.model.Scene;
import com.yumistories.repository.SceneRepository;
import com.yumistories.repository.StoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class SceneService {

    private final SceneRepository sceneRepository;
    private final StoryRepository storyRepository;

    public SceneService(SceneRepository sceneRepository, StoryRepository storyRepository) {
        this.sceneRepository = sceneRepository;
        this.storyRepository = storyRepository;
    }

    public List<Scene> getScenesForStory(Long storyId) {
        return sceneRepository.findByStoryIdOrderByOrderIndexAsc(storyId);
    }

    public Scene addScene(Long storyId, String text) {
        if (!storyRepository.existsById(storyId)) {
            throw new NoSuchElementException("Story not found: " + storyId);
        }

        // Следующий порядковый номер = сколько сцен уже есть у этой истории.
        // Просто и достаточно для MVP; если позже понадобится вставка
        // сцены "в середину" — усложним эту логику.
        int nextOrderIndex = sceneRepository.findByStoryIdOrderByOrderIndexAsc(storyId).size();

        Scene scene = new Scene(storyId, nextOrderIndex, text);
        return sceneRepository.save(scene);
    }
}