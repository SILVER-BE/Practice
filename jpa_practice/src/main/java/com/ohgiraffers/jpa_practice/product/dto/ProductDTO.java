package com.ohgiraffers.jpa_practice.product.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class ProductDTO {

    private int productCode;
    private String productName;
    private int originCost;
    private LocalDateTime releaseDate;
    private int discountRate;
    private int salesQuantity;
    private int stockQuantity;
    private int categoryCode;
    private String productionStatus;

}
