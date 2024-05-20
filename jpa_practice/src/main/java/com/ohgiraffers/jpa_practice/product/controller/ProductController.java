package com.ohgiraffers.jpa_practice.product.controller;

import com.ohgiraffers.jpa_practice.common.Pagenation;
import com.ohgiraffers.jpa_practice.common.PagingButton;
import com.ohgiraffers.jpa_practice.product.dto.CategoryDTO;
import com.ohgiraffers.jpa_practice.product.dto.ProductDTO;
import com.ohgiraffers.jpa_practice.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/{productCode}")
    public String findProductByCode(@PathVariable int productCode, Model model) {

        //엔티티로 받지 않고, DTO로 받는다!
        ProductDTO resultProduct = productService.findProductByProductCode(productCode);
        model.addAttribute("product", resultProduct);

        return "product/detail";
    }

    @GetMapping("/list")
    public String findProdctList(Model model, @PageableDefault Pageable pageable) {

        Page<ProductDTO> productList = productService.findProductList(pageable);
        PagingButton paging = Pagenation.getPagingButtonInfo(productList);

        model.addAttribute("productList", productList);
        model.addAttribute("paging", paging);

        return "product/list";
    }

    @GetMapping("/querymethod")
    public void querymethodPage() {}

    @GetMapping("/search")
    public String findByOriginCost(@RequestParam Integer originCost, Model model) {

        List<ProductDTO> productList = productService.findByOriginCost(originCost);
        model.addAttribute("productList", productList);

        return "product/searchResult";
    }

    @GetMapping("/regist")
    public void registPage() {}

    @GetMapping("/category")
    @ResponseBody
    public List<CategoryDTO> findCategoryList() {
        return productService.findAllCategory();
    }

    @PostMapping("/regist")
    public String registNewProduct(@ModelAttribute ProductDTO productDTO) {

        productService.registProduct(productDTO);

        return "redirect:/product/list";
    }


    @GetMapping("/modify")
    public void modifyProduct() {}

    @PostMapping("/modify")
    public String modifyProduct(@ModelAttribute ProductDTO productDTO) {

        productService.modifyProduct(productDTO);

        return "redirect:/product/" + productDTO.getProductCode();

    }

    @GetMapping("/delete")
    public void deleteProduct() {}

    @PostMapping("/delete")
    public String deleteProduct(@RequestParam Integer productCode) {

        productService.deleteProduct(productCode);

        return "redirect:/product/list";
    }


}
