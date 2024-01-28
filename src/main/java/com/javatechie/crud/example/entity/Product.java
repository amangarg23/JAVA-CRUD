package com.javatechie.crud.example.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "PRODUCT_TBL")
@RedisHash("Product")

public class Product implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
//    @GeneratedValue
    public int id;
    private String name;
    private int quantity;
    private double price;
}