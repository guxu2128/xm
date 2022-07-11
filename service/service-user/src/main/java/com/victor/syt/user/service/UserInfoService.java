package com.victor.syt.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.victor.syt.model.user.UserInfo;
import com.victor.syt.vo.user.LoginVo;

import java.util.Map;

public interface UserInfoService extends IService<UserInfo> {
    Map<String, Object> login(LoginVo loginVo);
}
