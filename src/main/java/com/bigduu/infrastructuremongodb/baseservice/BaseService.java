package com.bigduu.infrastructuremongodb.baseservice;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.Optional;

public interface BaseService<T> {
    Optional<T> getById(String id);
    Collection<T> getAll();
    Collection<T> getAll(T t);
    void deleteOne(T t);
    Optional<T> update(T t);
    Optional<T> addOne(T t);
    Page<T> getAllByExampleAndPageable(T t, Pageable pageable);
}
