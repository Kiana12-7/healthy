package org.example.api.service;

import com.alibaba.dashscope.aigc.generation.GenerationResult;
import org.example.api.dto.AIPlanResponseDTO;
import org.example.api.entity.FitnessForm;
import org.example.api.entity.WorkoutPlan;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
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
    void buildPromptIncludesDescriptionAndVideoList() {
        FitnessForm form = new FitnessForm();
        form.setDescription("想减脂，每周训练五天");
        when(videoService.getAvailableVideosString()).thenReturn("俯卧撑, 深蹲");

        String prompt = instance.buildPrompt(form);

        assertTrue(prompt.contains("想减脂，每周训练五天"));
        assertTrue(prompt.contains("俯卧撑, 深蹲"));
        assertTrue(prompt.contains("dayNumber = 1 表示计划创建当天开始训练"));
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

    @Test
    void generatePlanUsesCurrentDateInsteadOfAiReturnedDates() {
        AIPlanGeneratorImpl spyInstance = spy(instance);
        GenerationResult generationResult = Mockito.mock(GenerationResult.class, RETURNS_DEEP_STUBS);
        WorkoutPlan savedPlan = new WorkoutPlan();
        savedPlan.setId(9L);

        FitnessForm form = new FitnessForm();
        form.setId(6L);
        form.setDescription("减脂");

        when(videoService.getAvailableVideosString()).thenReturn("俯卧撑");
        when(generationResult.getOutput().getChoices().get(0).getMessage().getContent()).thenReturn("""
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
                        {"actionName": "俯卧撑", "orderInDay": 1}
                      ]
                    }
                  ]
                }
                """);
        doReturn(generationResult).when(spyInstance).callOpenAI(any());
        when(workoutPlanService.save(any(LocalDate.class), any(LocalDate.class), eq(6L))).thenReturn(savedPlan);

        spyInstance.generatePlan(form);

        ArgumentCaptor<LocalDate> startDateCaptor = ArgumentCaptor.forClass(LocalDate.class);
        ArgumentCaptor<LocalDate> endDateCaptor = ArgumentCaptor.forClass(LocalDate.class);
        verify(workoutPlanService).save(startDateCaptor.capture(), endDateCaptor.capture(), eq(6L));
        verify(planDetailService).saveAllByAIPlan(eq(9L), anyList());

        assertEquals(LocalDate.now(), startDateCaptor.getValue());
        assertEquals(LocalDate.now().plusDays(27), endDateCaptor.getValue());
    }
}
