package com.majestic.pkg.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.majestic.pkg.entity.UserSessionEntity;
import com.majestic.pkg.enums.SessionType;

import java.util.List;

/**
 * @Author: huangpenglong
 * @Date: 2023/3/17 15:33
 */
public interface UserSessionService extends IService<UserSessionEntity>{

    /**
     * 新增会话
     * @param userId
     * @param sessionName
     * @param sessionType
     * @return
     */
    UserSessionEntity save(String userId, String sessionName, SessionType sessionType);

    /**
     * 查询会话
     * @param userId
     * @param sessionType
     * @return
     */
    List<UserSessionEntity> getSessionList(String userId, SessionType sessionType);
}
