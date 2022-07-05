package com.victor.syt.hosp.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.victor.syt.model.hosp.Schedule;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface ScheduleService {
    void save(HttpServletRequest request);

    Page<Schedule> selectPage(HttpServletRequest request);

    void remove(HttpServletRequest request);
}
