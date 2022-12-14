package ru.itis.videoattention.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.videoattention.model.dto.VideoDto;
import ru.itis.videoattention.service.VideoService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class VideoController {

	private final VideoService videoService;

	@PostMapping("/video-attention")
	public ResponseEntity<UUID> handleVideo(@RequestParam("video") MultipartFile video) {
		return ResponseEntity.ok(videoService.handleVideo(video));
	}

	@GetMapping("/video-status")
	public String getVideoStatus(@RequestParam("file-id") String fileId) {
		return videoService.getVideoStatus(fileId);
	}

	@GetMapping("/video-status/all")
	public List<VideoDto> getAllStatuses() {
		return videoService.getAllVideoStatus();
	}
}
