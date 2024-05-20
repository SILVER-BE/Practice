package com.ohgiraffers.jpa_practice.product.repository;

import com.ohgiraffers.jpa_practice.product.entity.Product;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    /* 파라미터로 전달 받은 가격을 초과하는 제품 목록 조회 */
    List<Product> findByOriginCostGreaterThan(Integer originCost);

    /* 파라미터로 전달 받은 가격을 초과하는 제품을 가격순으로 조회 */
    List<Product> findByOriginCostGreaterThanOrderByOriginCost(Integer originCost);

    /* 파라미터로 전달 받은 가격을 초과하는 제품 목록을 정렬하여 조회 */
    List<Product> findByOriginCostGreaterThan(Integer originCost, Sort sort);

}
