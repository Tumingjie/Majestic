package com.majestic.pkg.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.majestic.pkg.entity.UserAdvicesEntity;
import com.majestic.pkg.mapper.UserAdvicesMapper;
import com.majestic.pkg.service.UserAdvicesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserAdvicesServiceImpl extends ServiceImpl<UserAdvicesMapper, UserAdvicesEntity>
        implements UserAdvicesService {
}
