package com.yumistories.repository;

import com.yumistories.model.Scene;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SceneRepository extends JpaRepository<Scene, Long> {

    // Spring Data сам реализует этот метод по одному только его названию:
    // "найти все Scene с данным storyId, отсортировав по orderIndex".
    List<Scene> findByStoryIdOrderByOrderIndexAsc(Long storyId);
}