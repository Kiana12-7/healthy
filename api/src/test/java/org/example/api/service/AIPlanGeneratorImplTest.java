package org.example.api.service;

import org.example.api.dto.AIPlanResponseDTO;
import org.example.api.entity.FitnessForm;
import org.example.api.enums.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.Mockito.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

class AIPlanGeneratorImplTest {

    private AIPlanGeneratorImpl instance;
    private VideoService videoService;
    private WorkoutPlanService workoutPlanService;
    private PlanDetailService planDetailService;

    @BeforeEach
    void setUp() {
        this.videoService = Mockito.mock(VideoService.class);
        this.workoutPlanService = Mockito.mock(WorkoutPlanService.class);
        this.planDetailService = Mockito.mock(PlanDetailService.class);
        this.instance = new AIPlanGeneratorImpl(videoService, workoutPlanService, planDetailService);
    }

    @Test
    void callOpenAI() {
        FitnessForm form = new FitnessForm();
        form.setTrainLocation(TrainLocation.CORE.getValue());
        form.setPartState(PartState.FLABBY.getValue());
        form.setHeight("170");
        form.setBodyType(BodyType.APPLE.getValue());
        form.setHurtLocation(HurtLocation.NONE.getValue());
        form.setPassionSport(PassionSport.NONE.getValue());
        when(videoService.getAvailableVideosString()).thenReturn( "bench_press_video.mp4"+
                "bicep_curls_video.mp4",
                "crunches_video.mp4"+
                "deadlifts_video.mp4"+
                "lunges-video.mp4"+
                "planks-video.mp4"+
                "pull-ups_video.mp4"+
                "push-ups-video.mp4"+
                "rows_video.mp4"+
                "shoulder_press_video.mp4"+
                "squat_video.mp4");
        instance.callOpenAI( """
            请根  据以下用户信息生成一份个性化训练计划，输出JSON格式。
            
            用户信息（来自 FitnessForm 实体）：
            - 训练部位(trainLocation)：%s 当前状态(partState)：%s
            - 身高(height)：%s cm
            - 体型(bodyType)：%s
            - 受伤部位(hurtLocation)：%s
            - 计划类型(planType)：%s
            - 喜爱运动(passionSport)：%s
            
            【重要约束】
            你只能从下面提供的可用动作列表中选择动作，不得使用列表之外的动作名称。
            
            可用动作列表：
            %s
            
            生成规则：
            1. 计划总时长为28天，每周训练5天。
            2. 必须避开受伤部位（如膝盖受伤则避免深蹲、跳跃动作）。
            3. 根据训练地点选择动作：家→徒手或小哑铃；健身房→可使用器械。
            4. 每天安排4~6个动作，每个动作必须来自上面的可用列表。
            5. 输出严格JSON格式，字段使用驼峰命名，示例：
            {
              "planType": "减脂",
              "durationDays": 28,
              "startDate": "2026-04-08",
              "endDate": "2026-05-05",
              "weeklyFrequency": 5,
              "details": [
                {
                  "dayNumber": 1,
                  "videos": [
                    {"actionName": "bench_press_video", "orderInDay": 1},
                    {"actionName": "bicep_curls_video", "orderInDay": 2}
                  ]
                }
              ]
            }
            """.formatted(
                form.getTrainLocation(), PartState.fromByte(form.getPartState()), form.getHeight(),
                BodyType.fromByte(form.getBodyType()), HurtLocation.fromByte(form.getHurtLocation()),
                PlanType.fromByte(form.getPlanType()), PassionSport.fromByte(form.getPassionSport()),
                videoService.getAvailableVideosString()
        ));
    }

    @Test
    void parseResponse() {
        AIPlanResponseDTO result = instance.parseResponse("""
                                          {
                                      "planType": "减脂",
                                      "durationDays": 28,
                                      "startDate": "2026-04-08",
                                      "endDate": "2026-05-05",
                                      "weeklyFrequency": 5,
                                      "details": [
                                        {
                                          "dayNumber": 1,
                                          "videos": [
                                            {"actionName": "bench_press_video", "orderInDay": 1},
                                            {"actionName": "bicep_curls_video", "orderInDay": 2}
                                          ]
                                        }
                                      ]
                                    }
                                    """);
        // 顶层字段断言
        assertNotNull(result);
        assertNotNull(result.getPlanType());
        assertEquals("减脂", result.getPlanType());
        assertNotNull(result.getDurationDays());
        assertEquals(28, result.getDurationDays());
        assertNotNull(result.getStartDate());
        assertEquals("2026-04-08", result.getStartDate());
        assertNotNull(result.getEndDate());
        assertEquals("2026-05-05", result.getEndDate());
        assertNotNull(result.getWeeklyFrequency());
        assertEquals(5, result.getWeeklyFrequency());

        // details 列表断言
        assertNotNull(result.getDetails());
        assertEquals(1, result.getDetails().size());

        AIPlanResponseDTO.DayDetail dayDetail = result.getDetails().get(0);
        assertNotNull(dayDetail);
        assertNotNull(dayDetail.getDayNumber());
        assertEquals(1, dayDetail.getDayNumber());

        // videos 列表断言
        List<AIPlanResponseDTO.Video> videos = dayDetail.getVideos();
        assertNotNull(videos);
        assertEquals(2, videos.size());

        // 第一个视频断言
        AIPlanResponseDTO.Video video1 = videos.get(0);
        assertNotNull(video1);
        assertNotNull(video1.getActionName());
        assertEquals("bench_press_video", video1.getActionName());
        assertNotNull(video1.getOrderInDay());
        assertEquals(1, video1.getOrderInDay());

        // 第二个视频断言
        AIPlanResponseDTO.Video video2 = videos.get(1);
        assertNotNull(video2);
        assertNotNull(video2.getActionName());
        assertEquals("bicep_curls_video", video2.getActionName());
        assertNotNull(video2.getOrderInDay());
        assertEquals(2, video2.getOrderInDay());
    }
}