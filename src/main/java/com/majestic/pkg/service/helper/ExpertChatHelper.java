package com.majestic.pkg.service.helper;

import com.majestic.pkg.api.openai.ChatGPTApi;
import com.majestic.pkg.api.openai.enums.Role;
import com.majestic.pkg.entity.SessionChatRecordEntity;
import com.majestic.pkg.entity.UserSessionEntity;
import com.majestic.pkg.service.PromptService;
import com.majestic.pkg.service.SessionChatRecordService;
import com.majestic.pkg.service.UserSessionService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author :wuxiaodong
 * @Date: 2023/4/24 19:18
 * @Description:会话名拆解、prompt构建、持久化聊天记录这些逻辑
 */
@Component
public class ExpertChatHelper {
    @Resource
    SessionChatRecordService sessionChatRecordService;
    @Resource
    PromptService promptService;
    @Resource
    UserSessionService userSessionService;

    private ExpertChatHelper() {}
    public boolean handleSessionSystemRecord(UserSessionEntity userSessionEntity) {
        String[] split = userSessionEntity.getSessionName().split(":");
        String prompt = promptService.getByTopic(split[split.length - 2]);
        SessionChatRecordEntity sessionChatRecordEntity = new SessionChatRecordEntity(
                userSessionEntity.getSessionId(), Role.SYSTEM.name, prompt, ChatGPTApi.getMessageTokenNum(prompt));
        return sessionChatRecordService.save(sessionChatRecordEntity);
    }

    public String getExpertChatLanguage(Integer sessionId) {
        UserSessionEntity userSessionEntity = userSessionService.getById(sessionId);
        String[] split = userSessionEntity.getSessionName().split(":");
        return split[split.length - 1];
    }
}
