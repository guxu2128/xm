package com.victor.syt.msm.service;

import com.victor.syt.vo.msm.MsmVo;

public interface MsmService {
    //发送手机验证码
    void send(String phone);
    boolean send(MsmVo msmVo);
}
