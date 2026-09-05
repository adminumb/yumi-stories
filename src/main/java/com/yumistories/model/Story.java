package com.yumistories.model;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;

import java.util.ArrayList;
import java.util.List;

/**
 * Одна история про Юми: заголовок + короткая идея, из которой позже
 * будут сгенерированы сцены.
 * <p>
 * @Entity говорит Spring Data JPA: "это не просто класс, а описание таблицы
 * в базе данных". При старте приложения Hibernate сам создаст таблицу
 * "stories" с колонками, соответствующими полям ниже.
 */
@Entity
public class Story {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    // @Lob — для текста, который может быть длиннее обычной VARCHAR-колонки
    @Lob
    private String idea;

    // @ElementCollection — простая коллекция строк, не отдельная сущность.
    // Spring Data сам создаст вспомогательную таблицу story_photos
    // (story_id, filename) для хранения имён файлов.
    // fetch = EAGER — загружаем фото сразу вместе с историей, без этого
    // при возврате в JSON можно получить ошибку "не удалось загрузить
    // ленивую коллекцию" (сессия к тому моменту уже закрыта).
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "story_photos", joinColumns = @JoinColumn(name = "story_id"))
    @Column(name = "filename")
    private List<String> photoFilenames = new ArrayList<>();

    // JPA требует пустой конструктор — он используется Hibernate
    // при чтении записей из базы данных.
    protected Story() {
    }

    public Story(String title, String idea) {
        this.title = title;
        this.idea = idea;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIdea() {
        return idea;
    }

    public void setIdea(String idea) {
        this.idea = idea;
    }

    public List<String> getPhotoFilenames() {
        return photoFilenames;
    }

    public void addPhotoFilename(String filename) {
        this.photoFilenames.add(filename);
    }
}
