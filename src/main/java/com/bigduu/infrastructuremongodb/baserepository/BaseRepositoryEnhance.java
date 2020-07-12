package com.bigduu.infrastructuremongodb.baserepository;


import com.bigduu.infrastructuremongodb.baseentity.BaseMongoEntity;

/**
 * @author bigduu
 * @title: BaseRepositoryEnhance
 * @description: TODO
 * @date 2020/5/405:21
 */
public interface BaseRepositoryEnhance<T extends BaseMongoEntity> {
    T softDelete(T entity);
}
