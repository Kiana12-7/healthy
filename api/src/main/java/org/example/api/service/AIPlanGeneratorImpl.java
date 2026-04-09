package org.example.api.service;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.dashscope.protocol.Protocol;
import lombok.Setter;
import org.example.api.dto.AIPlanResponseDTO;
import org.example.api.entity.FitnessForm;
import org.example.api.entity.WorkoutPlan;
import org.example.api.enums.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;

@Service
public class AIPlanGeneratorImpl implements AIPlanGenerator{
    @Value("${qianwen.api.key}")
    private String apiKey;
    private final VideoService videoService;
    private static final String OPENAI_URL = "https://dashscope.aliyuncs.com/compatible-mode/v1";
    @Setter
    private WorkoutPlanService workoutPlanService;
    private final PlanDetailService planDetailService;

    public AIPlanGeneratorImpl(VideoService videoService,
                               PlanDetailService planDetailService) {
        this.videoService = videoService;
        this.planDetailService = planDetailService;
    }

    @Override
    public WorkoutPlan generatePlan(FitnessForm form) {
        // 获取ai生成的json数据，并转化为实体
        String prompt = buildPrompt(form);
        String jsonResponse = callOpenAI(prompt).getOutput().getChoices().get(0).getMessage().getContent();
        AIPlanResponseDTO dto = parseResponse(jsonResponse);
        LocalDate startTime = LocalDate.parse(dto.getStartDate());
        LocalDate endTime = LocalDate.parse(dto.getEndDate());
        // 保存训练计划
        WorkoutPlan workoutPlan = this.workoutPlanService.save(startTime, endTime, form.getId());
        // 批量保存训练详情
        this.planDetailService.saveAllByAIPlan(workoutPlan.getId(), dto.getDetails());

        // todo 重试机制
        return null;
    }

    @Override
    public String buildPrompt(FitnessForm form) {
        return """
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
                    );
    }

    @Override
    public GenerationResult callOpenAI(String prompt) throws ApiException {
        Generation gen = new Generation(Protocol.HTTP.getValue(), OPENAI_URL);
        Message systemMsg = Message.builder()
                .role(Role.SYSTEM.getValue())
                .content("你是一位资深健身教练。")
                .build();
        Message userMsg = Message.builder()
                .role(Role.USER.getValue())
                .content(prompt)
                .build();
        GenerationParam param = GenerationParam.builder()
                .apiKey(apiKey)
                .model("qwen-plus")
                .messages(Arrays.asList(systemMsg, userMsg))
                .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                .build();
        try {
            GenerationResult result = gen.call(param);
            System.out.println(result.getOutput().getChoices().get(0).getMessage().getContent());
            return result;
        } catch (ApiException | NoApiKeyException | InputRequiredException e) {
            System.err.println("错误信息："+e.getMessage());
            System.out.println("请参考文档：https://www.alibabacloud.com/help/model-studio/developer-reference/error-code");
        }
        return null;
    }

    @Override
    public AIPlanResponseDTO parseResponse(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, AIPlanResponseDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse AI response", e);
        }
    }
}
