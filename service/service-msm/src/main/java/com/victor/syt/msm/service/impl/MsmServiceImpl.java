package com.victor.syt.msm.service.impl;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.tea.TeaException;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.Common;
import com.aliyun.teautil.models.RuntimeOptions;
import com.victor.commen.exception.GeneralException;
import com.victor.commen.result.ResultCodeEnum;
import com.victor.commen.utils.RandomUtil;
import com.victor.syt.msm.service.MsmService;
import com.victor.syt.msm.utils.ConstantPropertiesUtils;
import com.victor.syt.vo.msm.MsmVo;
import io.jsonwebtoken.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class MsmServiceImpl implements MsmService {
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Override
    public void send(String phone) {
        Client client;
        String code = RandomUtil.getSixBitRandom();
        String codeExacted = redisTemplate.opsForValue().get(phone);
        if(!StringUtils.isEmpty(codeExacted) || "".equals(codeExacted)) {
            throw new GeneralException(ResultCodeEnum.MESSAGE_EXACTED);
        }
        try {
            client = createClient(ConstantPropertiesUtils.ACCESS_KEY_ID, ConstantPropertiesUtils.SECRET);
        } catch (Exception e) {
            throw new GeneralException(ResultCodeEnum.MESSAGE_CLIENT_FAIL);
        }
        SendSmsRequest sendSmsRequest = new SendSmsRequest()
                .setSignName("阿里云短信测试")
                .setTemplateCode("SMS_154950909")
                .setPhoneNumbers(phone)
                .setTemplateParam("{\"code\":\"" + code + "\"}");
        RuntimeOptions runtime = new RuntimeOptions();
        try {
            SendSmsResponse sendSmsResponse = client.sendSmsWithOptions(sendSmsRequest, runtime);
            // 复制代码运行请自行打印 API 的返回值
            log.info("Result code: " + sendSmsResponse.getBody().code + "Result massage: " + sendSmsResponse.getBody().message);
            Assert.isTrue("OK".equals(sendSmsResponse.getBody().message));
        } catch (TeaException error) {
            Common.assertAsString(error.message);
        } catch (Exception _error) {
            throw new GeneralException(ResultCodeEnum.MESSAGE_SENT_FAIL);
        }
        redisTemplate.opsForValue().set(phone,code, 1L, TimeUnit.MINUTES);
    }

    @Override
    public boolean send(MsmVo msmVo) {
        if(!StringUtils.isEmpty(msmVo.getPhone())) {
            this.send(msmVo.getPhone());
            return true;
        }
        return false;
    }


    public static Client createClient(String accessKeyId, String accessKeySecret) throws Exception {
        Config config = new Config()
                // 您的 AccessKey ID
                .setAccessKeyId(accessKeyId)
                // 您的 AccessKey Secret
                .setAccessKeySecret(accessKeySecret);
        // 访问的域名
        config.endpoint = "dysmsapi.aliyuncs.com";
        return new Client(config);
    }
}
