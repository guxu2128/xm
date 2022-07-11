package com.victor.syt.hosp.service;

import com.victor.syt.model.hosp.Hospital;
import com.victor.syt.vo.hosp.HospitalQueryVo;

import javax.servlet.http.HttpServletRequest;

public interface HospitalService {
    void save(HttpServletRequest request);
    Hospital getByHoscode(HttpServletRequest request);

    Object selectPage(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo);

    void updateStatus(String id, Integer status);

    Object show(String id);

    String getHospName(String hoscode);
}
