package org.example.api.startup;

import org.example.api.entity.Video;
import org.example.api.repository.VideoRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * 初始化视频资源
 * */
@Component
public class V002InitVideo  implements CommandLineRunner, Ordered {
    // 执行顺序
    public static final int order = 101;
    private final VideoRepository videoRepository;
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
            "squat_video.mp4"
    ));

    public V002InitVideo(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

    @Override
    public void run(String @NonNull ... args) throws Exception {

        // 遍历视频路径列表，保存到数据库
        this.videoPaths.forEach(path -> {
            Video video = new Video();
            video.setUrl(path);

            int lastDot = path.lastIndexOf(".");
            if (lastDot > 0) {
                String withoutExt = path.substring(0, lastDot);
                video.setTitle(withoutExt);
            } else {
                video.setTitle(path);  // 没有扩展名时直接用原路径
            }

            this.videoRepository.save(video);
        });

    }

    @Override
    public int getOrder() {
        return order;
    }
}
