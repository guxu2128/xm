package com.victor.syt.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.victor.cmn.client.DictFeignClient;
import com.victor.commen.exception.GeneralException;
import com.victor.commen.helper.HttpRequestHelper;
import com.victor.commen.result.ResultCodeEnum;
import com.victor.syt.enums.DictEnum;
import com.victor.syt.hosp.repository.HospitalRepository;
import com.victor.syt.hosp.service.HospitalService;
import com.victor.syt.hosp.service.HospitalSetService;
import com.victor.syt.model.hosp.Hospital;
import com.victor.syt.vo.hosp.HospitalQueryVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class HospitalServiceImpl implements HospitalService {

    @Autowired
    private HospitalRepository hospitalRepository;
    @Autowired
    private HospitalSetService hospitalSetService;
    @Autowired
    private DictFeignClient dictFeignClient;

    @Override
    public void save(HttpServletRequest request) {
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());
        String hoscode = (String)paramMap.get("hoscode");
        if(StringUtils.isEmpty(hoscode)) {
            throw new GeneralException(ResultCodeEnum.PARAM_ERROR);
        }
        String logoDataString = (String)paramMap.get("logoData");
        if(!StringUtils.isEmpty(logoDataString)) {
            String logoData = logoDataString.replaceAll(" ", "+");
            paramMap.put("logoData", logoData);
        }
        if(!HttpRequestHelper.isSignEquals(paramMap, hospitalSetService.getSignKey(hoscode))) {
            throw new GeneralException(ResultCodeEnum.SIGN_ERROR);
        }
        Hospital hospital = JSONObject.parseObject(JSONObject.toJSONString(paramMap),Hospital.class);
        Hospital targetHospital = hospitalRepository.getHospitalByHoscode(hospital.getHoscode());
        if(null != targetHospital) {
            hospital.setStatus(targetHospital.getStatus());
            hospital.setCreateTime(targetHospital.getCreateTime());
        } else {
            hospital.setStatus(0);
            hospital.setCreateTime(new Date());
        }
        hospital.setUpdateTime(new Date());
        hospital.setIsDeleted(0);
        saveHospital(hospital);
    }

    @Override
    public Hospital getByHoscode(HttpServletRequest request) {
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());
        String hoscode = (String)paramMap.get("hoscode");
        if(StringUtils.isEmpty(hoscode)) {
            throw new GeneralException(ResultCodeEnum.PARAM_ERROR);
        }
        if(!HttpRequestHelper.isSignEquals(paramMap, hospitalSetService.getSignKey(hoscode))) {
            throw new GeneralException(ResultCodeEnum.SIGN_ERROR);
        }
        return hospitalRepository.getHospitalByHoscode(hoscode);
    }

    public void saveHospital(Hospital hospital) {
        try {
            hospitalRepository.save(hospital);
        }catch (Exception e) {
            throw new GeneralException(ResultCodeEnum.DATA_INSERT_ERROR);
        }
    }
    @Override
    public Page<Hospital> selectPage(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
//0为第一页
        Pageable pageable = PageRequest.of(page-1, limit, sort);

        Hospital hospital = new Hospital();
        BeanUtils.copyProperties(hospitalQueryVo, hospital);

//创建匹配器，即如何使用查询条件
        ExampleMatcher matcher = ExampleMatcher.matching() //构建对象
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING) //改变默认字符串匹配方式：模糊查询
                .withIgnoreCase(true); //改变默认大小写忽略方式：忽略大小写

        //创建实例
        Example<Hospital> example = Example.of(hospital, matcher);
        Page<Hospital> pages = (Page<Hospital>) hospitalRepository.findAll(example, pageable);
        pages.getRecords().stream().forEach(item -> {
            this.packHospital(item);
        });
        return pages;
    }

    @Override
    public void updateStatus(String id, Integer status) {
        if(status.intValue() == 0 || status.intValue() == 1) {
            Hospital hospital = hospitalRepository.findById(id).get();
            hospital.setStatus(status);
            hospital.setUpdateTime(new Date());
            hospitalRepository.save(hospital);
        }
    }

    @Override
    public Map<String, Object> show(String id) {
        Map<String, Object> result = new HashMap<>();
        Hospital hospital = this.packHospital(hospitalRepository.findById(id).get());
        result.put("hospital", hospital);
//单独处理更直观
        result.put("bookingRule", hospital.getBookingRule());
//不需要重复返回
        hospital.setBookingRule(null);
        return result;
    }



    private Hospital packHospital(Hospital hospital) {
        String hostypeString = dictFeignClient.getName(DictEnum.HOSTYPE.getDictCode(),hospital.getHostype());
        String provinceString = dictFeignClient.getName(hospital.getProvinceCode());
        String cityString = dictFeignClient.getName(hospital.getCityCode());
        String districtString = dictFeignClient.getName(hospital.getDistrictCode());

        hospital.getParam().put("hostypeString", hostypeString);
        hospital.getParam().put("fullAddress", provinceString + cityString + districtString + hospital.getAddress());
        return hospital;
    }

}
