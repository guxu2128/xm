package com.victor.syt.hosp.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.victor.syt.model.hosp.Schedule;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface ScheduleService {
    void save(HttpServletRequest request);

    Page<Schedule> selectPage(HttpServletRequest request);
    Map<String, Object> getRuleSchedule(long page, long limit, String hoscode, String depcode);
    void remove(HttpServletRequest request);

    List<Schedule> getDetailSchedule(String hoscode, String depcode, String workDate);
}
