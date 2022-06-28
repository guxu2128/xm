package com.victor.commen.utils;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BaseTransfer <T,VO> {


    private Class<T> clazzT;
    private Class<VO> clazzVO;

    public BaseTransfer() {
        Type types=this.getClass().getGenericSuperclass();
        Type[] genericType=((ParameterizedType)types).getActualTypeArguments();
        clazzT=(Class<T>) genericType[0];
        clazzVO=(Class<VO>)genericType[1];
    }

    /**
     * T转Vo
     */
    public VO toVo(T t){
        try {
            VO vo = clazzVO.newInstance();
            BeanUtils.copyProperties(t,vo);
            return vo;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 转T
     */
    public T toEntity(VO vo){

        try {
            T t = clazzT.newInstance();
            BeanUtils.copyProperties(vo,t);
            return t;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 集合转Vo
     */

    public List<VO> toVo(List<T> list){
        ArrayList<VO> vos = new ArrayList<>();
        list.forEach(t->{
            VO vo = toVo(t);
            vos.add(vo);
        });
        return vos;
    }

    /**
     * 集合转T
     */
    public List<T> toEntity(List<VO> list){
        ArrayList<T> ts = new ArrayList<>();
        list.forEach(vo->{
            T t = toEntity(vo);
            ts.add(t);
        });
        return ts;
    }
}
