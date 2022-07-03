package com.victor.syt.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.victor.commen.exception.GeneralException;
import com.victor.commen.helper.HttpRequestHelper;
import com.victor.commen.result.ResultCodeEnum;
import com.victor.syt.hosp.repository.HospitalRepository;
import com.victor.syt.hosp.service.HospitalService;
import com.victor.syt.hosp.service.HospitalSetService;
import com.victor.syt.model.hosp.Hospital;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

@Service
@Slf4j
public class HospitalServiceImpl implements HospitalService {

    @Autowired
    private HospitalRepository hospitalRepository;
    @Autowired
    private HospitalSetService hospitalSetService;

    @Override
    public void save(HttpServletRequest request) {
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());
        //必须参数校验 略
        String hoscode = (String)paramMap.get("hoscode");
        if(StringUtils.isEmpty(hoscode)) {
            throw new GeneralException(ResultCodeEnum.PARAM_ERROR);
        }
//签名校验
        if(!HttpRequestHelper.isSignEquals(paramMap, hospitalSetService.getSignKey(hoscode))) {
            throw new GeneralException(ResultCodeEnum.SIGN_ERROR);
        }
        Hospital hospital = JSONObject.parseObject(JSONObject.toJSONString(paramMap),Hospital.class);
        //判断是否存在
        Hospital targetHospital = hospitalRepository.getHospitalByHoscode(hospital.getHoscode());
        if(null != targetHospital) {
            hospital.setStatus(targetHospital.getStatus());
            hospital.setCreateTime(targetHospital.getCreateTime());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
            try {
                hospitalRepository.save(hospital);
            }catch (Exception e) {
                throw new GeneralException(ResultCodeEnum.DATA_INSERT_ERROR);
            }

        } else {
//0：未上线 1：已上线
            hospital.setStatus(0);
            hospital.setCreateTime(new Date());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
            try {
                hospitalRepository.save(hospital);
            }catch (Exception e) {
                throw new GeneralException(ResultCodeEnum.DATA_INSERT_ERROR);
            }
        }

    }

}
