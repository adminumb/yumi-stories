package com.yumistories.repository;

import com.yumistories.model.Story;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Никакой реализации писать не нужно — Spring Data JPA на старте приложения
 * сам создаст класс-реализацию с методами findAll(), save(), findById(),
 * deleteById() и т.д., исходя из типов <Story, Long> (сущность, тип id).
 */
public interface StoryRepository extends JpaRepository<Story, Long> {
}
