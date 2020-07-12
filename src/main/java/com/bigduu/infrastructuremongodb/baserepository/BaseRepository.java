package com.bigduu.infrastructuremongodb.baserepository;

import com.bigduu.infrastructuremongodb.baseentity.BaseMongoEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * @author bigduu
 * @title: BaseRepository
 * @description: TODO
 * @date 2020/5/405:20
 */
@NoRepositoryBean
public interface BaseRepository<T extends BaseMongoEntity> extends MongoRepository<T, String>,BaseRepositoryEnhance<T>  {
}
