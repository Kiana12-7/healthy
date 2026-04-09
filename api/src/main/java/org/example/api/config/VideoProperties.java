package org.example.api.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "video")
public class VideoProperties {

    /**
     * 视频本地路径（相对路径）
     */
    private String localPath;

    /**
     * 视频访问 URL
     */
    private String webUrl;

    /**
     * 封面访问 URL
     */
    private String coverWebUrl;
}
