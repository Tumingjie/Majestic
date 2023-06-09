package com.majestic.pkg.controller;

import com.google.common.collect.ImmutableList;
import com.majestic.pkg.api.openai.constant.OpenAIConst;
import com.majestic.pkg.api.openai.enums.Role;
import com.majestic.pkg.api.openai.req.ChatGPTReq;
import com.majestic.pkg.api.openai.req.ContextMessage;
import com.majestic.pkg.api.openai.resp.ChatGPTResp;
import com.majestic.pkg.dto.TranslationRequest;
import com.majestic.pkg.entity.UserApiKeyEntity;
import com.majestic.pkg.enums.ApiType;
import com.majestic.pkg.enums.Prompt;
import com.majestic.pkg.service.AdminApiKeyService;
import com.majestic.pkg.service.ChatService;
import com.majestic.pkg.service.PromptService;
import com.majestic.pkg.service.UserApiKeyService;
import com.majestic.pkg.utils.ResultCode;
import com.majestic.pkg.utils.ReturnResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @Author : oujiajun
 * @Date: 2023/3/30
 * @Description: 翻译
 */

@RestController
@CrossOrigin
@Slf4j
@RequestMapping("/translation")
public class TranslationController {
    private static final String NAME_MESSAGE = "translateResult";

    @Resource
    private ChatService chatService;
    @Resource
    private AdminApiKeyService adminApiKeyService;
    @Resource
    private PromptService promptService;
    @Resource
    private UserApiKeyService userApiKeyService;

    /**
     * 翻译
     * @param req
     * @return
     */
    @PostMapping("/translate")
    public ReturnResult translate(@RequestBody @Valid TranslationRequest req) {
        if (StringUtils.isEmpty(req.getMessage()) || req.getUserId() == null) {
            return ReturnResult.error().codeAndMessage(ResultCode.EMPTY_PARAM);
        }

        String reqMsg = String.format(promptService.getByTopic(Prompt.TRANSLATE.topic), req.getLanguage(), req.getMessage());

        // 若用户上传了apikey则使用用户的，否则采用本系统的
        UserApiKeyEntity userApiKeyEntity = userApiKeyService.getByUserIdAndType(req.getUserId(), ApiType.OPENAI);
        String apiKey = userApiKeyEntity != null && !StringUtils.isEmpty(userApiKeyEntity.getApikey())
                ? userApiKeyEntity.getApikey()
                : adminApiKeyService.roundRobinGetByType(ApiType.OPENAI);
        if(apiKey == null){
            return ReturnResult.error().codeAndMessage(ResultCode.ADMIN_APIKEY_NULL);
        }

        // 调用对话接口
        ChatGPTReq gptReq = ChatGPTReq.builder()
                .messages(ImmutableList.of(new ContextMessage(Role.USER.name, reqMsg)))
                .model(OpenAIConst.MODEL_NAME_CHATGPT_3_5)
                .build();

        ChatGPTResp resp = chatService.oneShotChat(req.getUserId(), gptReq, apiKey);

        if (resp.getMessage() == null) {
            return ReturnResult.error();
        }
        return ReturnResult.ok().data(NAME_MESSAGE, resp.getMessage());
    }

}
