package com.victor.syt.hosp.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.victor.syt.model.hosp.HospitalSet;
import com.victor.syt.vo.hosp.HospitalSetInsertVo;
import com.victor.syt.vo.hosp.HospitalSetQueryVo;
import com.victor.syt.vo.order.SignInfoVo;


public interface HospitalSetService extends IService<HospitalSet> {
    Page<HospitalSet> findPageHospSet(long current, long limit, HospitalSetQueryVo hospitalSetQueryVo);
    void lockHospitalSetById(Long id, Integer status);
    void removeById(Long id);
    void save(HospitalSetInsertVo hospitalSetInsetVo);
    void updateById(HospitalSetInsertVo hospitalSetInsetVo);
    String getSignKey(String hoscode);
    //获取医院签名信息
    SignInfoVo getSignInfoVo(String hoscode);

}
