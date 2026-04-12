package org.example.api.service;

import com.alibaba.dashscope.aigc.generation.GenerationResult;
import org.example.api.entity.FitnessForm;
import org.example.api.entity.User;
import org.example.api.repository.FitnessFormRepository;
import org.springframework.stereotype.Service;

@Service
public class AIChatServiceImpl implements AIChatService {
    private final AIPlanGenerator aiPlanGenerator;
    private final UserService userService;
    private final FitnessFormRepository fitnessFormRepository;

    public AIChatServiceImpl(AIPlanGenerator aiPlanGenerator,
                             UserService userService,
                             FitnessFormRepository fitnessFormRepository) {
        this.aiPlanGenerator = aiPlanGenerator;
        this.userService = userService;
        this.fitnessFormRepository = fitnessFormRepository;
    }

    @Override
    public String ask(String message) {
        User currentUser = userService.getCurrentLoginUserDetails();
        FitnessForm fitnessForm = fitnessFormRepository.findByUser(currentUser).orElse(null);

        String prompt = buildPrompt(message, fitnessForm);
        GenerationResult result = aiPlanGenerator.callOpenAI(prompt);
        if (result == null || result.getOutput() == null || result.getOutput().getChoices() == null || result.getOutput().getChoices().isEmpty()) {
            throw new RuntimeException("AI 未返回有效内容");
        }
        return result.getOutput().getChoices().get(0).getMessage().getContent();
    }

    private String buildPrompt(String message, FitnessForm fitnessForm) {
        String fitnessContext = fitnessForm != null && fitnessForm.getDescription() != null && !fitnessForm.getDescription().isBlank()
                ? "当前用户的健身信息如下：\n" + fitnessForm.getDescription() + "\n"
                : "当前用户暂未填写完整健身信息，请先基于通用健身知识回答。\n";

        return """
                你是一位专业的健身教练，请直接回答用户的健身问题。
                回答要求：
                1. 优先给出明确、可执行的建议。
                2. 不要输出 JSON。
                3. 如果用户的问题和训练计划制定相关，但没有明确要求你立即制定完整计划，可以先解释思路并提示用户发送“制定计划”。
                4. 回答简洁、自然，适合移动端聊天界面阅读。
                
                %s
                用户问题：
                %s
                """.formatted(fitnessContext, message);
    }
}
