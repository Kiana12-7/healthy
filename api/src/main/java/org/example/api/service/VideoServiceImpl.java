package org.example.api.service;

import org.example.api.entity.Video;
import org.example.api.repository.VideoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VideoServiceImpl implements VideoService{
    private final VideoRepository videoRepository;

    public VideoServiceImpl(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

     @Override
     public String getAvailableVideosString() {
        List<Video> videos = (List<Video>) videoRepository.findAll();
        return videos.stream()
                .map(v -> String.format("%s (video_id=%d)", v.getTitle(), v.getId()))
                .collect(Collectors.joining(", "));
    }
}
