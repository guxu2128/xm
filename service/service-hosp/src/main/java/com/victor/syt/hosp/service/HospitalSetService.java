package com.victor.syt.hosp.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.victor.syt.model.hosp.HospitalSet;
import com.victor.syt.vo.hosp.HospitalSetQueryVo;

public interface HospitalSetService extends IService<HospitalSet> {
    Page<HospitalSet> findPageHospSet(long current, long limit, HospitalSetQueryVo hospitalSetQueryVo);
}
