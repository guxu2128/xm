package com.victor.syt.hosp.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.victor.syt.model.hosp.Schedule;
import com.victor.syt.vo.hosp.ScheduleOrderVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface ScheduleService {
    void save(HttpServletRequest request);

    Page<Schedule> selectPage(HttpServletRequest request);
    Map<String, Object> getRuleSchedule(long page, long limit, String hoscode, String depcode);
    void remove(HttpServletRequest request);

    List<Schedule> getDetailSchedule(String hoscode, String depcode, String workDate);

    Map<String, Object> getBookingScheduleRule(Integer page, Integer limit, String hoscode, String depcode);

    Schedule getById(String scheduleId);
    //根据排班id获取预约下单数据
    ScheduleOrderVo getScheduleOrderVo(String scheduleId);
    /**
     * 修改排班
     */
    void update(Schedule schedule);

}
