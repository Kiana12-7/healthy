package org.example.api.dto;

import lombok.Data;

@Data
public class VideoDto {
    private Integer id;
    private String title;       // 标题
    private String author;      // 作者
    private String coverUrl;    // 封面
    private String videoUrl;    // 视频地址（http://xxx.mp4）
    private String duration;    // 时长
    private String level;       // 等级
}
