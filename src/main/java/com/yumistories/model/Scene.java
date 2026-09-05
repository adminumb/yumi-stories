package com.yumistories.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

/**
 * Одна сцена внутри истории. Намеренно используем простое поле storyId
 * (Long), а не JPA-связь @ManyToOne на Story — это проще для чтения
 * на старте и избегает вопросов сериализации вложенных объектов в JSON.
 * Если позже понадобятся более сложные запросы (например, "все сцены
 * с фото Юми в дождь"), можно будет перейти на настоящую связь.
 */
@Entity
public class Scene {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long storyId;

    // Порядковый номер сцены внутри истории: 0, 1, 2, ...
    private int orderIndex;

    @Lob
    private String text;

    protected Scene() {
    }

    public Scene(Long storyId, int orderIndex, String text) {
        this.storyId = storyId;
        this.orderIndex = orderIndex;
        this.text = text;
    }

    public Long getId() {
        return id;
    }

    public Long getStoryId() {
        return storyId;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public String getText() {
        return text;
    }
}
