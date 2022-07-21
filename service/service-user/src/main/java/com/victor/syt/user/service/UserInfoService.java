package com.victor.syt.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.victor.syt.model.user.UserInfo;
import com.victor.syt.vo.user.LoginVo;
import com.victor.syt.vo.user.UserAuthVo;
import com.victor.syt.vo.user.UserInfoQueryVo;

import java.util.Map;

public interface UserInfoService extends IService<UserInfo> {
    Map<String, Object> login(LoginVo loginVo);
    //用户认证
    void userAuth(Long userId, UserAuthVo userAuthVo);

    IPage<UserInfo> selectPage(Page<UserInfo> pageParam, UserInfoQueryVo userInfoQueryVo);

    void approval(Long userId, Integer authStatus);
}
