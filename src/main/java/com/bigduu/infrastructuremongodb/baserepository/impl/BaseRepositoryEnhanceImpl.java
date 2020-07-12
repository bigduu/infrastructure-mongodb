package com.bigduu.infrastructuremongodb.baserepository.impl;

import com.bigduu.infrastructuremongodb.baseentity.BaseMongoEntity;
import com.bigduu.infrastructuremongodb.baserepository.BaseRepositoryEnhance;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.data.mongodb.repository.support.SimpleMongoRepository;
import org.springframework.util.Assert;

import java.util.Optional;

/**
 * @author bigduu
 * @title: BaseRepositoryEnhanceImpl
 * @description: TODO
 * @date 2020/5/405:22
 */
public class BaseRepositoryEnhanceImpl<T extends BaseMongoEntity> extends SimpleMongoRepository<T, String> implements BaseRepositoryEnhance<T> {
    private final MongoOperations mongoOperations;
    private final MongoEntityInformation<T, String> entityInformation;
    /**
     * Creates a new {@link SimpleMongoRepository} for the given {@link MongoEntityInformation} and {@link MongoTemplate}.
     *
     * @param metadata        must not be {@literal null}.
     * @param mongoOperations must not be {@literal null}.
     */
    public BaseRepositoryEnhanceImpl(MongoEntityInformation<T, String> metadata, MongoOperations mongoOperations) {
        super(metadata, mongoOperations);
        this.mongoOperations = mongoOperations;
        this.entityInformation = metadata;
    }

    @Override
    public T softDelete(T entity) {
        entity.setDataStatus(true);
        return mongoOperations.save(entity,entityInformation.getCollectionName());
    }

    @Override
    public Optional<T> findById(String id) {
        Assert.notNull(id, "The given id must not be null!");
        return  Optional.ofNullable(mongoOperations.findOne(new Query(Criteria.where("_id").is(id).and("isDeleted").is(false)),entityInformation.getJavaType()));
    }
}
