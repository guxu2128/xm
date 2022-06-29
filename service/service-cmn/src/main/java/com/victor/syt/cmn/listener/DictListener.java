package com.victor.syt.cmn.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.victor.syt.cmn.mapper.DictMapper;
import com.victor.syt.model.cmn.Dict;
import com.victor.syt.vo.cmn.DictEeVo;
import org.springframework.beans.BeanUtils;

import java.util.LinkedList;
import java.util.List;

public class DictListener extends AnalysisEventListener<DictEeVo> {

    private DictMapper dictMapper;

    public List<Long> getFailList() {
        return failList;
    }

    private List<Long> failList;
    public DictListener(DictMapper dictMapper) {
        this.dictMapper = dictMapper;
        this.failList = new LinkedList<Long>();
    }

    //一行一行读取
    @Override
    public void invoke(DictEeVo dictEeVo, AnalysisContext analysisContext) {
        //调用方法添加数据库
        Dict dict = new Dict();
        BeanUtils.copyProperties(dictEeVo,dict);
        try {
            dictMapper.insert(dict);
        }catch (Exception e) {
            failList.add(dict.getId());
        }
    }
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
    }
}
