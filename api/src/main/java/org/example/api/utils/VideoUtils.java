package org.example.api.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;

public class VideoUtils {
    // 视频存放根目录
    private static final String VIDEO_BASE_PATH = "D:/team/healthy/api/video/";
    // 封面存放目录
    private static final String COVER_BASE_PATH = "D:/team/healthy/api/video/covers/";

    static {
        // 启动时确保封面目录存在
        try {
            Files.createDirectories(Paths.get(COVER_BASE_PATH));
        } catch (IOException e) {
            System.err.println("创建封面目录失败：" + e.getMessage());
        }
    }

    /**
     * 截取视频第一帧作为封面
     * @param videoFileName 视频文件名 (如 "bench_press_video.mp4")
     * @return 生成的封面图片文件名 (如 "bench_press_video.jpg")，失败返回 null
     */
    public static String extractCover(String videoFileName) {
        File videoFile = new File(VIDEO_BASE_PATH, videoFileName);
        if (!videoFile.exists()) {
            System.err.println("视频文件不存在: " + videoFileName);
            return null;
        }

        // 生成封面文件名 (把 .mp4 换成 .jpg)
        String coverFileName = videoFileName.substring(0, videoFileName.lastIndexOf('.')) + ".jpg";
        File coverFile = new File(COVER_BASE_PATH, coverFileName);

        // 如果封面已经存在，直接返回，不用重复截取
        if (coverFile.exists()) {
            return coverFileName;
        }

        try {
            // 构建 FFmpeg 命令
            // -i: 输入文件
            // -y: 覆盖输出文件
            // -vframes 1: 只截取第1帧
            // -ss 00:00:01: 从第1秒开始截（避免黑屏）
            ProcessBuilder processBuilder = new ProcessBuilder(
                    "D:/team/ffmpeg-8.1-full_build/bin/ffmpeg.exe",
                    "-i", videoFile.getAbsolutePath(),
                    "-ss", "00:00:01",
                    "-vframes", "1",
                    "-y",
                    coverFile.getAbsolutePath()
            );

            // 启动进程并等待完成
            Process process = processBuilder.start();

            // 消费掉输入流和错误流，防止进程卡死
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
            System.err.println("截取封面时发生异常：" + e.getMessage());
            return null;
        }
    }

    private static void consumeProcessStream(Process process) {
        // 读取错误流
        new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                while (reader.readLine() != null) {
                    // 不打印，只读取，消除空循环警告
                }
            } catch (IOException ignored) {}
        }).start();

        // 读取标准流
        new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                while (reader.readLine() != null) {
                    // 不打印，只读取
                }
            } catch (IOException ignored) {}
        }).start();
    }
}
