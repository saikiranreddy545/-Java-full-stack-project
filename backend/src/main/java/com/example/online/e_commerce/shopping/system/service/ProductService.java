package com.example.online.e_commerce.shopping.system.service;

import com.example.online.e_commerce.shopping.system.model.Product;
import com.example.online.e_commerce.shopping.system.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public List<Product> getFeaturedProducts() {
        return productRepository.findByFeaturedTrue();
    }

    public List<Product> getRecentProducts() {
        return productRepository.findTop8ByOrderByCreatedAtDesc();
    }

    public List<Product> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    public List<Product> searchProducts(String query, Long categoryId, String sort) {
        List<Product> products;

        boolean hasQuery = query != null && !query.trim().isEmpty();
        boolean hasCategory = categoryId != null && categoryId > 0;

        if (hasQuery && hasCategory) {
            products = productRepository.searchByCategoryIdAndQuery(categoryId, query.trim());
        } else if (hasQuery) {
            products = productRepository.searchProducts(query.trim());
        } else if (hasCategory) {
            products = productRepository.findByCategoryId(categoryId);
        } else {
            products = productRepository.findAll();
        }

        // Sorting
        if (sort != null) {
            switch (sort) {
                case "price_asc":
                    products.sort(Comparator.comparing(Product::getPrice));
                    break;
                case "price_desc":
                    products.sort(Comparator.comparing(Product::getPrice).reversed());
                    break;
                case "name_asc":
                    products.sort(Comparator.comparing(Product::getName));
                    break;
                case "rating":
                    products.sort(Comparator.comparing(Product::getRating, Comparator.nullsLast(Comparator.reverseOrder())));
                    break;
                case "newest":
                default:
                    products.sort(Comparator.comparing(Product::getCreatedAt, Comparator.nullsLast(Comparator.reverseOrder())));
                    break;
            }
        }

        return products;
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public boolean decreaseStock(Long productId, int quantity) {
        Optional<Product> prodOpt = productRepository.findById(productId);
        if (prodOpt.isPresent()) {
            Product product = prodOpt.get();
            if (product.getStockQuantity() >= quantity) {
                product.setStockQuantity(product.getStockQuantity() - quantity);
                productRepository.save(product);
                return true;
            }
        }
        return false;
    }

    public long countProducts() {
        return productRepository.count();
    }

    public List<Product> getLowStockProducts(int threshold) {
        return productRepository.findByStockQuantityLessThan(threshold);
    }
}
