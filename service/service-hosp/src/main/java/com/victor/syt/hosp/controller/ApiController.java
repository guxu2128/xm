package com.victor.syt.hosp.controller;

import com.victor.commen.helper.HttpRequestHelper;
import com.victor.commen.result.Result;
import com.victor.syt.hosp.service.HospitalService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Api(tags = "医院管理接口")
@RestController
@RequestMapping("/admin/hosp/api")
public class ApiController {
    @Autowired
    private HospitalService hospitalService;

    @ApiOperation(value = "上传医院")
    @PostMapping("saveHospital")
    public Result saveHospital(HttpServletRequest request) {
        hospitalService.save(request);
        return Result.ok();
    }

}
