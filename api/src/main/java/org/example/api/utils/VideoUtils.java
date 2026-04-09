package org.example.api.utils;

import org.example.api.config.VideoProperties;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class VideoUtils {
    // 从配置注入
    private final VideoProperties videoProperties;
    private String videoBasePath;
    private String coverBasePath;

    // 注入配置
    public VideoUtils(VideoProperties videoProperties) {
        this.videoProperties = videoProperties;
    }

    /**
     * 初始化：自动拼接正确路径
     */
    @PostConstruct
    public void init() {
        String apiPath = System.getProperty("user.dir");
        this.videoBasePath = apiPath + File.separator + videoProperties.getLocalPath();
        this.coverBasePath = videoBasePath + File.separator + "covers";

        // 启动时确保封面目录存在
        try {
            Files.createDirectories(Paths.get(coverBasePath));
            System.out.println("封面目录已创建：" + coverBasePath);
        } catch (IOException e) {
            System.err.println("创建封面目录失败：" + e.getMessage());
        }
    }

    /**
     * 截取视频第一帧作为封面
     */
    public String extractCover(String videoFileName) {
        File videoFile = new File(videoBasePath, videoFileName);
        if (!videoFile.exists()) {
            System.err.println("视频文件不存在: " + videoFileName);
            return null;
        }

        // 生成封面文件名
        String coverFileName = videoFileName.substring(0, videoFileName.lastIndexOf('.')) + ".jpg";
        File coverFile = new File(coverBasePath, coverFileName);

        if (coverFile.exists()) {
            return coverFileName;
        }

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(
                    "ffmpeg", // 去掉了硬编码路径，系统环境变量里的ffmpeg即可
                    "-i", videoFile.getAbsolutePath(),
                    "-ss", "00:00:01",
                    "-vframes", "1",
                    "-y",
                    coverFile.getAbsolutePath()
            );

            Process process = processBuilder.start();
            consumeProcessStream(process);
            int exitCode = process.waitFor();

            if (exitCode == 0 && coverFile.exists()) {
                System.out.println("封面截取成功: " + coverFileName);
                return coverFileName;
            } else {
                System.err.println("封面截取失败，FFmpeg 退出码: " + exitCode);
                return null;
            }

        } catch (Exception e) {
            System.err.println("截取封面异常：" + e.getMessage());
            return null;
        }
    }

    private void consumeProcessStream(Process process) {
        new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                while (reader.readLine() != null) {}
            } catch (IOException ignored) {}
        }).start();

        new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                while (reader.readLine() != null) {}
            } catch (IOException ignored) {}
        }).start();
    }
}