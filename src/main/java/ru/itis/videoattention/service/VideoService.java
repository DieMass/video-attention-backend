package ru.itis.videoattention.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.videoattention.model.dto.VideoDto;
import ru.itis.videoattention.model.entity.VideoEntity;
import ru.itis.videoattention.repository.VideoRepository;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VideoService {

	private final RestTemplate restTemplate;
	private final VideoRepository videoRepository;

	@Value("${python.url.post}")
	private String pythonUrlPostVideo;

	@Value("${python.url.status}")
	private String pythonUrlStatus;
	private final Gson gson;
	@SneakyThrows
	public UUID handleVideo(MultipartFile file) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		MultiValueMap<String, Object> body
				= new LinkedMultiValueMap<>();
		body.add("video",  new ByteArrayResource(file.getBytes()));
		HttpEntity<MultiValueMap<String, Object>> requestEntity
				= new HttpEntity<>(body, headers);

		Map<String, String> response = restTemplate.postForObject(pythonUrlPostVideo, requestEntity, Map.class);
		UUID fileId = UUID.fromString(response.get("file_id:"));
		videoRepository.save(VideoEntity.builder().fileId(fileId).build());
		return fileId;
	}

	public String getVideoStatus(String fileId) {
		return gson.fromJson(restTemplate.getForObject(pythonUrlStatus, String.class, fileId), Map.class).get("status").toString();
	}

	public List<VideoDto> getAllVideoStatus() {
		return videoRepository.findAll().stream()
				.map(entity -> VideoDto.builder().fileId(entity.getFileId()).build())
				.peek(dto -> dto.setStatus(getVideoStatus(dto.getFileId().toString())))
				.collect(Collectors.toList());
	}
}
