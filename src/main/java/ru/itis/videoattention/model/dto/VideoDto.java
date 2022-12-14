package ru.itis.videoattention.model.dto;

import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class VideoDto {

	private UUID fileId;
	private  String status;

}
