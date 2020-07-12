package com.bigduu.infrastructuremongodb.basecontroller;

import com.bigduu.infrastructuremongodb.baseentity.BaseMongoEntity;
import com.bigduu.infrastructuremongodb.baseservice.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import java.util.Collection;

public abstract class BaseController<T extends BaseMongoEntity> {
    protected BaseService<T> service;

    public BaseController(BaseService<T> service) {
        this.service = service;
    }


    @GetMapping("/{id}")
    public T getById(@PathVariable String id) {
        return service.getById(id).orElse(null);
    }

    @GetMapping("/example")
    public Collection<T> getAll(T t) {
        return service.getAll(t);
    }

    @PermitAll
    @GetMapping("/example/pageable")
    public Page<T> getAllByExampleAndPageable(T t, @RequestParam(value = "page", defaultValue = "0") Integer page,
                                              @RequestParam(value = "limit", defaultValue = "10") Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC,"dataCreatedDate"));
        return service.getAllByExampleAndPageable(t,pageable);
    }

    @GetMapping
    public Collection<T> getAll() {
        return service.getAll();
    }

    @DeleteMapping
    public void deleteById(@RequestBody T t) {
        service.deleteOne(t);
    }

    @PutMapping("/{id}")
    public T updateById(@PathVariable String id, @RequestBody T t) {
        return service.update(t).orElse(null);
    }

    @PutMapping
    public T update(@RequestBody T t) {
        return service.update(t).orElse(null);
    }


    @PostMapping
    public T addOne(@RequestBody T t) {
        return service.addOne(t).orElse(null);
    }


}
