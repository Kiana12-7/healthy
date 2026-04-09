package org.example.api.service;

import org.example.api.dto.VideoDto;
import org.example.api.entity.Video;
import org.example.api.repository.VideoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {
    private final VideoRepository videoRepository;

    public CourseServiceImpl(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

    @Override
    public List<VideoDto> getVideoList() {
        // 从数据库查数据
        List<Video> videos = (List<Video>) videoRepository.findAll();

        // 转 DTO
        return videos.stream().map(video -> {
            VideoDto dto = new VideoDto();
            dto.setId(video.getId().intValue());
            dto.setTitle(video.getTitle());
            dto.setAuthor("System");
            dto.setCoverUrl(video.getCoverUrl());
            dto.setVideoUrl(video.getUrl());
            dto.setDuration(video.getDuration());
            dto.setLevel("K1");
            return dto;
        }).collect(Collectors.toList());
    }
}
