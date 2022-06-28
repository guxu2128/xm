package com.victor.syt.hosp.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.victor.commen.result.Result;
import com.victor.syt.hosp.service.HospitalSetService;
import com.victor.syt.model.hosp.HospitalSet;
import com.victor.syt.vo.hosp.HospitalSetInsertVo;
import com.victor.syt.vo.hosp.HospitalSetQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Api(tags = "医院设置管理")
@RestController
@RequestMapping("/admin/hosp/hospitalSet")
public class HospitalSetController {

    //注入service
    @Autowired
    private HospitalSetService hospitalSetService;

    //1 查询医院设置表所有信息
    @ApiOperation(value = "获取所有医院设置")
    @GetMapping("findAll")
    public Result findAllHospitalSet() {
        //调用service的方法
        List<HospitalSet> list = hospitalSetService.list();
        return Result.ok(list);
    }

    //2 逻辑删除医院设置
    @ApiOperation(value = "逻辑删除医院设置")
    @DeleteMapping("{id}")
    public Result removeHospSet(@PathVariable Long id) {
        hospitalSetService.removeById(id);
        return Result.ok();
    }

    //3 条件查询带分页
    @PostMapping("findPageHospSet/{current}/{limit}")
    public Result findPageHospSet(@PathVariable long current,
                                  @PathVariable long limit,
                                  @RequestBody
                                          (required = false) HospitalSetQueryVo hospitalSetQueryVo) {

        Page<HospitalSet> pageHospitalSet = hospitalSetService.findPageHospSet(current, limit, hospitalSetQueryVo);
        //返回结果
        return Result.ok(pageHospitalSet);
    }

    //4 添加医院设置
    @PostMapping("saveHospitalSet")
    public Result saveHospitalSet(@RequestBody HospitalSetInsertVo hospitalSetInsertVo) {
        //调用service
        hospitalSetService.save(hospitalSetInsertVo);
        return Result.ok();
    }

    //5 根据id获取医院设置
    @GetMapping("getHospSet/{id}")
    public Result getHospSet(@PathVariable Long id) {
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        return Result.ok(hospitalSet);
    }

    //6 修改医院设置
    @PostMapping("updateHospitalSet")
    public Result updateHospitalSet(@RequestBody HospitalSetInsertVo hospitalSetInsertVo) {
        hospitalSetService.updateById(hospitalSetInsertVo);
        return Result.ok();
    }

    //7 批量删除医院设置
    @DeleteMapping("batchRemove")
    public Result batchRemoveHospitalSet(@RequestBody List<Long> idList) {
        hospitalSetService.removeByIds(idList);
        return Result.ok();
    }

    //8 医院设置锁定和解锁
    @PutMapping("lockHospitalSet/{id}/{status}")
    public Result lockHospitalSet(@PathVariable Long id,
                                  @PathVariable Integer status) {
        hospitalSetService.lockHospitalSetById(id, status);
        return Result.ok();
    }

}
