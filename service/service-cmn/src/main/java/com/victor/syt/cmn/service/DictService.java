package com.victor.syt.cmn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.victor.syt.model.cmn.Dict;
import org.springframework.stereotype.Service;

import java.util.List;

public interface DictService extends IService<Dict> {
    //根据数据id查询子数据列表
    List<Dict> findChildData(Long id);
}
