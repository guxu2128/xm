package com.victor.syt.hosp.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.victor.syt.model.hosp.Department;
import com.victor.syt.vo.hosp.DepartmentQueryVo;
import com.victor.syt.vo.hosp.DepartmentVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface DepartmentService {
    /**
     * 上传科室信息
     * @param paramMap
     */
    void save(HttpServletRequest request);
    Page<Department> selectPage(HttpServletRequest request);
    void remove(HttpServletRequest request);
    List<DepartmentVo> findDeptTree(String hoscode);

    Object getDepName(String hoscode, String depcode);
}