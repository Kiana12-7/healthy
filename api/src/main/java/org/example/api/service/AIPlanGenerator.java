package org.example.api.service;

import com.alibaba.dashscope.aigc.generation.GenerationResult;
import org.example.api.dto.AIPlanResponseDTO;
import org.example.api.entity.FitnessForm;
import org.example.api.entity.WorkoutPlan;

public interface AIPlanGenerator {
    /**
     * 生成计划，保存到数据库中
     * */
    WorkoutPlan generatePlan(FitnessForm form);

    /**
     *
     * 构建请求prompt
     * */
    String buildPrompt(FitnessForm form);

    /**
     * 请求ai
     * */
    GenerationResult callOpenAI(String prompt);

    /**
     * 解析返回的json
     * */
    AIPlanResponseDTO parseResponse(String json);

}
