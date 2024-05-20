package com.ohgiraffers.jpa_practice.product.service;

import com.ohgiraffers.jpa_practice.product.dto.CategoryDTO;
import com.ohgiraffers.jpa_practice.product.dto.ProductDTO;
import com.ohgiraffers.jpa_practice.product.entity.Category;
import com.ohgiraffers.jpa_practice.product.entity.Product;
import com.ohgiraffers.jpa_practice.product.repository.CategoryRepository;
import com.ohgiraffers.jpa_practice.product.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    /* 1. findById */
    public ProductDTO findProductByProductCode(int productCode) {
        Product foundProduct = productRepository.findById(productCode).orElseThrow(IllegalAccessError::new);

        return modelMapper.map(foundProduct, ProductDTO.class); // Entity -> DTO 로 가공
    }

    /* 2. findAll : Sort */
    public List<ProductDTO> findProductList() {
        List<Product> productList = productRepository.findAll(Sort.by("productCode").descending());

        return productList.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();

    }

    /* 3. findAll : Pageable */
    public Page<ProductDTO> findProductList(Pageable pageable) {
        pageable = PageRequest.of(
                pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() -1,   //현재 보고있는 페이지 (=offset) (실제 '1' 페이지면 여기선 '0'이어야 함)
                pageable.getPageSize(),     //한 페이지에 몇 개의 글을 볼 것인지 (=limit)
                Sort.by("productCode").descending()
        );
        Page<Product> productList = productRepository.findAll(pageable);
        return productList
                .map(product -> modelMapper.map(product, ProductDTO.class));
    }

    /* 4. Query Method */
    public List<ProductDTO> findByOriginCost(Integer originCost) {

        List<Product> productList = productRepository.findByOriginCostGreaterThan(
                originCost,
                Sort.by("originCost").descending()
        );

        return productList.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();
    }

    /* 5. JPQL or Native Query */
    public List<CategoryDTO> findAllCategory() {

        List<Category> categoryList = categoryRepository.findAllCategory();

        return categoryList.stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .toList();
    }

    /* 6. save */
    @Transactional
    public void registProduct(ProductDTO productDTO) {

        // DTO -> Entity
        productRepository.save(modelMapper.map(productDTO, Product.class));
    }

    /* 7. modify */
    @Transactional
    public void modifyProduct(ProductDTO productDTO) {

        Product foundProduct = productRepository.findById(productDTO.getProductCode()).orElseThrow(IllegalAccessError::new);

        foundProduct.modifyProductName(productDTO.getProductName());

    }

    /* 8. deleteById */
    @Transactional
    public void deleteProduct(Integer productCode) {

        productRepository.deleteById(productCode);
    }

}
