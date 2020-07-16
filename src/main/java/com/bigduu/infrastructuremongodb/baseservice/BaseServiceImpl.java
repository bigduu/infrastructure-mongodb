package com.bigduu.infrastructuremongodb.baseservice;

import com.bigduu.infrastructuremongodb.baseentity.BaseMongoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Optional;

public abstract class BaseServiceImpl<T extends BaseMongoEntity> implements BaseService<T> {

    @Autowired
    protected ApplicationEventPublisher publisher;

    protected MongoRepository<T, String> repository;

    public BaseServiceImpl(MongoRepository<T, String> repository){
        this.repository = repository;
    }

    @Override
    public Optional<T> getById(String id) {
        return repository.findById(id);
    }

    @Override
    public Collection<T> getAll() {
        return repository.findAll();
    }

    @Override
    public void deleteOne(T t) {
        beforeDelete(t);
        repository.deleteById(t.getId());
        deletedPublish(t);
    }

    public abstract void beforeCreate(T t);
    public abstract void beforeDelete(T t);
    public abstract void updatedPublish(T t);
    public abstract void deletedPublish(T t);
    public abstract void createdPublish(T t);

    @Override
    public Optional<T> update(T t) {
        T save = repository.save(t);
        updatedPublish(t);
        return Optional.of(save);
    }

    @Override
    public Optional<T> addOne(T t) {
        beforeCreate(t);
        T save = repository.save(t);
        createdPublish(save);
        return Optional.of(save);
    }

    @Override
    public Page<T> getAllByExampleAndPageable(T t, Pageable pageable) {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths("_class")
                .withIgnoreNullValues();// 忽略空值
        Example<T> of = Example.of(t,matcher);
        return repository.findAll(of,pageable);
    }

    @Override
    public Collection<T> getAll(T t) {
        Example<T> of = Example.of(t);
        return repository.findAll(of);
    }
}
