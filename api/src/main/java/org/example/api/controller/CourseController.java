package org.example.api.controller;

import org.example.api.dto.VideoDto;
import org.example.api.entity.Video;
import org.example.api.repository.VideoRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/course")
public class CourseController {
    private final VideoRepository videoRepository;

    public CourseController(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

    @GetMapping("/list")
    public List<VideoDto> getVideoList() {
        // 从数据库查出所有视频
        List<Video> videos = (List<Video>) videoRepository.findAll();

        // 转换成 DTO 返回给前端
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