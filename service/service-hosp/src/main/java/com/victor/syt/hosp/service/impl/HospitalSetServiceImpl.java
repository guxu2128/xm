package com.victor.syt.hosp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.victor.commen.exception.GeneralException;
import com.victor.commen.result.ResultCodeEnum;
import com.victor.commen.utils.MD5;
import com.victor.syt.hosp.mapper.HospitalSetMapper;
import com.victor.syt.hosp.service.HospitalSetService;
import com.victor.syt.model.hosp.HospitalSet;
import com.victor.syt.vo.hosp.HospitalSetInsertVo;
import com.victor.syt.vo.hosp.HospitalSetQueryVo;
import com.victor.syt.vo.order.SignInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.util.Random;
import org.springframework.beans.BeanUtils;

@Service
public class HospitalSetServiceImpl extends ServiceImpl<HospitalSetMapper, HospitalSet> implements HospitalSetService {

    @Autowired
    HospitalSetMapper hospitalSetMapper;
    @Override
    public Page<HospitalSet> findPageHospSet(long current, long limit, HospitalSetQueryVo hospitalSetQueryVo) {
        //创建page对象，传递当前页，每页记录数
        Page<HospitalSet> page = new Page<>(current,limit);
        //构建条件
        QueryWrapper<HospitalSet> wrapper = new QueryWrapper<>();
        String hosname = hospitalSetQueryVo.getHosname();//医院名称
        String hoscode = hospitalSetQueryVo.getHoscode();//医院编号
        if(!StringUtils.isEmpty(hosname)) {
            wrapper.like("hosname",hospitalSetQueryVo.getHosname());
        }
        if(!StringUtils.isEmpty(hoscode)) {
            wrapper.eq("hoscode",hospitalSetQueryVo.getHoscode());
        }
        //调用方法实现分页查询
        Page<HospitalSet> pageHospitalSet = page(page, wrapper);
        //返回结果
        return pageHospitalSet;
    }

    @Override
    public void lockHospitalSetById(Long id, Integer status) {
        HospitalSet hospitalSet = getById(id);
        //设置状态
        hospitalSet.setStatus(status);
        //调用方法
        updateById(hospitalSet);
    }

    @Override
    public void save(HospitalSetInsertVo hospitalSetInsertVo) {
        //设置状态 1 使用 0 不能使用
        hospitalSetInsertVo.setStatus(1);
        //签名秘钥
        Random random = new Random();
        hospitalSetInsertVo.setSignKey(MD5.encrypt(System.currentTimeMillis()+""+random.nextInt(1000)));
        HospitalSet hospitalSet = new HospitalSet();
        BeanUtils.copyProperties(hospitalSetInsertVo,hospitalSet);
        try {
            super.save(hospitalSet);
        } catch (Exception e){
            throw new GeneralException(ResultCodeEnum.DATA_INSERT_ERROR);
        }

    }

    @Override
    public void updateById(HospitalSetInsertVo hospitalSetInsertVo) {
        HospitalSet hospitalSet = new HospitalSet();
        BeanUtils.copyProperties(hospitalSetInsertVo,hospitalSet);
        Boolean flag = updateById(hospitalSet);
        if(!flag) {
            throw new GeneralException(ResultCodeEnum.DATA_INSERT_ERROR);
        }
    }

    @Override
    public String getSignKey(String hoscode) {
        HospitalSet hospitalSet = this.getByHoscode(hoscode);
        if(null == hospitalSet) {
            throw new GeneralException(ResultCodeEnum.HOSPITAL_OPEN);
        }
        if(hospitalSet.getStatus().intValue() == 0) {
            throw new GeneralException(ResultCodeEnum.HOSPITAL_LOCK);
        }
        return hospitalSet.getSignKey();
    }

    //获取医院签名信息
    @Override
    public SignInfoVo getSignInfoVo(String hoscode) {
        QueryWrapper<HospitalSet> wrapper = new QueryWrapper<>();
        wrapper.eq("hoscode",hoscode);
        HospitalSet hospitalSet = baseMapper.selectOne(wrapper);
        if(null == hospitalSet) {
            throw new GeneralException(ResultCodeEnum.HOSPITAL_OPEN);
        }
        SignInfoVo signInfoVo = new SignInfoVo();
        signInfoVo.setApiUrl(hospitalSet.getApiUrl());
        signInfoVo.setSignKey(hospitalSet.getSignKey());
        return signInfoVo;
    }


    /**
     * 根据hoscode获取医院设置
     * @param hoscode
     * @return
     */
    private HospitalSet getByHoscode(String hoscode) {
        return hospitalSetMapper.selectOne(new QueryWrapper<HospitalSet>().eq("hoscode", hoscode));
    }


    @Override
    public void removeById(Long id) {
        boolean flag = super.removeById(id);
        if (!flag){
            throw new GeneralException(ResultCodeEnum.DATA_REMOVE_ERROR);
        }
    }
}
