package com.bigduu.infrastructuremongodb.utils;

import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author YiHao
 * @Date 2020/4/15 10:32
 * @Description
 */
public class CopyVector<S, T> {
    private Class<T> targetType;

    public CopyVector(Class<T> targetType) {
        this.targetType = targetType;
    }

    public List<T> copyList(List<S> src) {
        List<T> target = new ArrayList<>();
        for (S s : src) {
            T t = BeanUtils.instantiateClass(targetType);
            BeanUtils.copyProperties(s, t);
            target.add(t);
        }
        return target;
    }
}

