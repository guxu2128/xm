package com.victor.syt.hosp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.victor.commen.exception.GeneralException;
import com.victor.commen.result.ResultCodeEnum;
import com.victor.commen.utils.BaseTransfer;
import com.victor.commen.utils.MD5;
import com.victor.syt.hosp.mapper.HospitalSetMapper;
import com.victor.syt.hosp.service.HospitalSetService;
import com.victor.syt.model.hosp.HospitalSet;
import com.victor.syt.vo.hosp.HospitalSetInsertVo;
import com.victor.syt.vo.hosp.HospitalSetQueryVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.util.Random;

@Service
public class HospitalSetServiceImpl extends ServiceImpl<HospitalSetMapper, HospitalSet> implements HospitalSetService {
    BaseTransfer baseTransfer = new BaseTransfer();

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
        super.save((HospitalSet) baseTransfer.toEntity(hospitalSetInsertVo));
    }

    @Override
    public void updateById(HospitalSetInsertVo hospitalSetInsertVo) {
        Boolean flag = updateById((HospitalSet) baseTransfer.toEntity(hospitalSetInsertVo));
        if(!flag) {
            throw new GeneralException(ResultCodeEnum.DATA_REMOVE_ERROR);
        }
    }

    @Override
    public void removeById(Long id) {
        boolean flag = super.removeById(id);
        if (!flag){
            throw new GeneralException(ResultCodeEnum.DATA_INSERT_ERROR);
        }
    }
}
