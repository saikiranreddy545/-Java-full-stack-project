package com.example.online.e_commerce.shopping.system.controller;

import com.example.online.e_commerce.shopping.system.model.Category;
import com.example.online.e_commerce.shopping.system.model.Product;
import com.example.online.e_commerce.shopping.system.service.CategoryService;
import com.example.online.e_commerce.shopping.system.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class HomeController {

    private final ProductService productService;
    private final CategoryService categoryService;

    public HomeController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("featuredProducts", productService.getFeaturedProducts());
        model.addAttribute("recentProducts", productService.getRecentProducts());
        return "index";
    }

    @GetMapping("/products")
    public String products(@RequestParam(name = "query", required = false) String query,
                           @RequestParam(name = "category", required = false) Long categoryId,
                           @RequestParam(name = "sort", required = false, defaultValue = "newest") String sort,
                           Model model) {
        List<Product> products = productService.searchProducts(query, categoryId, sort);
        model.addAttribute("products", products);
        model.addAttribute("searchQuery", query);
        model.addAttribute("selectedCategoryId", categoryId);
        model.addAttribute("currentSort", sort);

        if (categoryId != null && categoryId > 0) {
            categoryService.getCategoryById(categoryId).ifPresent(cat -> model.addAttribute("selectedCategory", cat));
        }

        return "products";
    }

    @GetMapping("/products/{id}")
    public String productDetail(@PathVariable("id") Long id, Model model) {
        Optional<Product> productOpt = productService.getProductById(id);
        if (productOpt.isEmpty()) {
            return "redirect:/products";
        }
        Product product = productOpt.get();
        model.addAttribute("product", product);

        // Fetch related products in the same category
        if (product.getCategory() != null) {
            List<Product> related = productService.getProductsByCategory(product.getCategory().getId())
                    .stream()
                    .filter(p -> !p.getId().equals(product.getId()))
                    .limit(4)
                    .toList();
            model.addAttribute("relatedProducts", related);
        }

        return "product-detail";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }
}
