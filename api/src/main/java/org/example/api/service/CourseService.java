package org.example.api.service;

import org.example.api.dto.VideoDto;

import java.util.List;

public interface CourseService {
    List<VideoDto> getVideoList();
}
