package com.ohgiraffers.jpa_practice.product.repository;

import com.ohgiraffers.jpa_practice.product.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    @Query(value = "SELECT c FROM Category c ORDER BY c.categoryCode")
    List<Category> findAllCategory();

}
