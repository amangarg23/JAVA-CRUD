package com.javatechie.crud.example.repository.redis;

import com.javatechie.crud.example.entity.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RedisProductRepository extends CrudRepository<Product, Integer> {

}
