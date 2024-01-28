package com.javatechie.crud.example.service;

import com.javatechie.crud.example.entity.Product;
import com.javatechie.crud.example.repository.jpa.ProductRepository;
import com.javatechie.crud.example.repository.redis.RedisProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    @Qualifier("productRepository")
    private ProductRepository repository;

    @Autowired
    @Qualifier("redisProductRepository")
    private RedisProductRepository redisRepository;

    // Save product to both SQL database and Redis cache
    public Product saveProduct(Product product) {
        Product savedProduct = repository.save(product);
        redisRepository.save(savedProduct); // Assuming RedisProductRepository extends CrudRepository
        return savedProduct;
    }

    // Save multiple products to both SQL database and Redis cache
    public List<Product> saveProducts(List<Product> products) {
        List<Product> savedProducts = repository.saveAll(products);
        redisRepository.saveAll(savedProducts); // Save to Redis as well
        return savedProducts;
    }

    // Retrieve products from Redis cache, fallback to SQL if not found
    public List<Product> getProducts() {
        Iterable<Product> cachedProductsIterable = redisRepository.findAll(); // Fetch from Redis
        List<Product> cachedProducts = new ArrayList<>();
        cachedProductsIterable.forEach(cachedProducts::add); // Convert Iterable to List

        if (cachedProducts.isEmpty()) {
            List<Product> sqlProducts = repository.findAll();
            redisRepository.saveAll(sqlProducts); // Update Redis cache
            return sqlProducts;
        }
        return cachedProducts;
    }

    // Retrieve product by id from Redis cache, fallback to SQL if not found
    public Product getProductById(int id) {
        return redisRepository.findById(id)
                .orElseGet(() -> {
                    Product sqlProduct = repository.findById(id).orElse(null);
                    if (sqlProduct != null) {
                        redisRepository.save(sqlProduct); // Update Redis cache
                    }
                    return sqlProduct;
                });
    }

    // Delete product from both SQL database and Redis cache
    public String deleteProduct(int id) {
        redisRepository.deleteById(id); // Remove from Redis cache
        repository.deleteById(id); // Remove from SQL database
        return "product removed !! " + id;
    }
}