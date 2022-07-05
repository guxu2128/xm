package com.victor.syt.hosp.service;

import com.victor.syt.model.hosp.Hospital;

import javax.servlet.http.HttpServletRequest;

public interface HospitalService {
    void save(HttpServletRequest request);
    Hospital getByHoscode(HttpServletRequest request);
}
