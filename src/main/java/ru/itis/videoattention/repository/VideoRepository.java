package ru.itis.videoattention.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.videoattention.model.entity.VideoEntity;

public interface VideoRepository extends JpaRepository<VideoEntity, Long> {

}
