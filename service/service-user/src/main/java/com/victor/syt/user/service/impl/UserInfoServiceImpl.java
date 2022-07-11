package com.victor.syt.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.victor.commen.exception.GeneralException;
import com.victor.commen.helper.JwtHelper;
import com.victor.commen.result.ResultCodeEnum;
import com.victor.syt.model.user.UserInfo;
import com.victor.syt.user.mapper.UserInfoMapper;
import com.victor.syt.user.service.UserInfoService;
import com.victor.syt.vo.user.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserInfoServiceImpl extends
        ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Override
    public Map<String, Object> login(LoginVo loginVo) {
        String phone = loginVo.getPhone();
        String code = loginVo.getCode();
        //校验参数
        if(StringUtils.isEmpty(phone) ||
                StringUtils.isEmpty(code)) {
            throw new GeneralException(ResultCodeEnum.PARAM_ERROR);
        }
        String codeExacted = redisTemplate.opsForValue().get(phone);
        if (!code.equals(codeExacted)) {
            throw new GeneralException(ResultCodeEnum.CODE_ERROR);
        }

        //手机号已被使用
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", phone);
        //获取会员
        UserInfo userInfo = baseMapper.selectOne(queryWrapper);
        if(null == userInfo) {
            userInfo = new UserInfo();
            userInfo.setName("");
            userInfo.setPhone(phone);
            userInfo.setStatus(1);
            this.save(userInfo);
        }
        //校验是否被禁用
        if(userInfo.getStatus() == 0) {
            throw new GeneralException(ResultCodeEnum.LOGIN_DISABLED_ERROR);
        }

        //返回页面显示名称
        Map<String, Object> map = new HashMap<>();
        String name = userInfo.getName();
        if(StringUtils.isEmpty(name)) {
            name = userInfo.getNickName();
        }
        if(StringUtils.isEmpty(name)) {
            name = userInfo.getPhone();
        }
        map.put("name", name);
        String token = JwtHelper.createToken(userInfo.getId(), name);
        map.put("token",token);
        return map;
    }
}

