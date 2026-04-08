package org.example.api.startup;

import org.example.api.config.VideoProperties;
import org.example.api.entity.Video;
import org.example.api.repository.VideoRepository;
import org.example.api.utils.VideoUtils;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import ws.schild.jave.MultimediaObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 初始化视频资源
 */
@Component
public class V002InitVideo implements CommandLineRunner, Ordered {
    public static final int order = 101;
    private final VideoRepository videoRepository;
    private final VideoProperties videoProperties;


    private final List<String> videoPaths = new ArrayList<>(Arrays.asList(
            "bench_press_video.mp4",
            "bicep_curls_video.mp4",
            "crunches_video.mp4",
            "deadlifts_video.mp4",
            "lunges-video.mp4",
            "planks-video.mp4",
            "pull-ups_video.mp4",
            "push-ups-video.mp4",
            "rows_video.mp4",
            "shoulder_press_video.mp4",
            "squats-video.mp4"
    ));

    public V002InitVideo(VideoRepository videoRepository, VideoProperties videoProperties) {
        this.videoRepository = videoRepository;
        this.videoProperties = videoProperties;
    }

    @Override
    public void run(String @NonNull ... args) {
        // 只在表为空时初始化，避免重复插入
        if (videoRepository.count() > 0) {
            System.out.println("视频表已存在数据，跳过初始化");
            return;
        }
        String apiPath = System.getProperty("user.dir");
        String fullVideoPath = apiPath + File.separator + videoProperties.getLocalPath();
        for (String path : videoPaths) {
            Video video = new Video();
            video.setUrl(videoProperties.getWebUrl() + path);
            video.setTitle(getChineseTitle(path));

            String coverFileName = VideoUtils.extractCover(path);
            video.setCoverUrl(coverFileName != null ? videoProperties.getCoverWebUrl() + coverFileName : "");

            // 获取时长
            Integer sec = getVideoDurationSeconds(fullVideoPath + File.separator + path);
            video.setDuration(sec);

            videoRepository.save(video);
        }

    }

    private Integer getVideoDurationSeconds(String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists()) return 0;

            MultimediaObject object = new MultimediaObject(file);
            long ms = object.getInfo().getDuration();
            return (int)(ms / 1000); // 直接返回秒
        } catch (Exception e) {
            return 0;
        }
    }


    /**
     * 视频文件名 → 中文标题
     */
    private String getChineseTitle(String fileName) {
        if (fileName.contains("bench_press")) return "卧推";
        if (fileName.contains("bicep_curls")) return "二头弯举";
        if (fileName.contains("crunches")) return "卷腹";
        if (fileName.contains("deadlifts")) return "硬拉";
        if (fileName.contains("lunges")) return "弓步蹲";
        if (fileName.contains("planks")) return "平板支撑";
        if (fileName.contains("pull-ups")) return "引体向上";
        if (fileName.contains("push-ups")) return "俯卧撑";
        if (fileName.contains("rows")) return "划船";
        if (fileName.contains("shoulder_press")) return "肩推";
        if (fileName.contains("squats")) return "深蹲";
        return "训练视频";
    }

    @Override
    public int getOrder() {
        return order;
    }
}