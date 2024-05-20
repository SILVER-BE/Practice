package com.ohgiraffers.jpa_practice.product.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "product_info")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto increment
    private int productCode;
    private String productName;
    private int originCost;
    private LocalDateTime releaseDate;
    private int discountRate;
    private int salesQuantity;
    private int stockQuantity;
    private int categoryCode;
    private String productionStatus;

    //메소드를 이용하여 이름을 바꾸도록 함!
    public void modifyProductName(String productName) {
        this.productName = productName;
    }
}
