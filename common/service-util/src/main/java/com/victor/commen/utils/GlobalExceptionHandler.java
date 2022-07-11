package com.victor.commen.utils;

import com.victor.commen.exception.GeneralException;
import com.victor.commen.result.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(Exception e){
        e.printStackTrace();
        return Result.fail();
    }

    /**
     * 自定义异常处理方法
     * @param e
     * @return
     */
    @ExceptionHandler(GeneralException.class)
    @ResponseBody
    public Result error(GeneralException e){
        e.printStackTrace();
        return Result.build(e.getCode(), e.getMessage());
    }
}
