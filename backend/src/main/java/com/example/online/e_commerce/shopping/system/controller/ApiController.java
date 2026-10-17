package com.example.online.e_commerce.shopping.system.controller;

import com.example.online.e_commerce.shopping.system.dto.CartDto;
import com.example.online.e_commerce.shopping.system.model.*;
import com.example.online.e_commerce.shopping.system.service.CategoryService;
import com.example.online.e_commerce.shopping.system.service.OrderService;
import com.example.online.e_commerce.shopping.system.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ApiController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final OrderService orderService;

    public ApiController(ProductService productService,
                         CategoryService categoryService,
                         OrderService orderService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.orderService = orderService;
    }

    @GetMapping("/products")
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable("id") Long id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/products")
    public ResponseEntity<Product> createProduct(@RequestBody Map<String, Object> body) {
        try {
            String name = (String) body.get("name");
            String description = (String) body.get("description");
            Double price = Double.valueOf(body.get("price").toString());
            Double originalPrice = body.get("originalPrice") != null ? Double.valueOf(body.get("originalPrice").toString()) : price;
            Integer stockQuantity = Integer.valueOf(body.get("stockQuantity").toString());
            String imageUrl = (String) body.get("imageUrl");
            Long categoryId = Long.valueOf(body.get("categoryId").toString());
            Boolean featured = body.get("featured") != null && Boolean.parseBoolean(body.get("featured").toString());

            Category category = categoryService.getCategoryById(categoryId).orElse(null);

            Product product = new Product(name, description, price, originalPrice, stockQuantity, imageUrl, 4.5, 10, featured, category);
            Product saved = productService.saveProduct(product);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Map<String, String>> deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
        Map<String, String> res = new HashMap<>();
        res.put("message", "Product deleted successfully");
        return ResponseEntity.ok(res);
    }

    @GetMapping("/categories")
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/orders")
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @PostMapping("/orders")
    public ResponseEntity<?> placeOrder(@RequestBody Map<String, Object> orderReq) {
        try {
            String name = (String) orderReq.get("customerName");
            String email = (String) orderReq.get("customerEmail");
            String phone = (String) orderReq.get("customerPhone");
            String address = (String) orderReq.get("shippingAddress");
            String city = (String) orderReq.get("city");
            String postalCode = (String) orderReq.get("postalCode");
            String paymentMethodStr = (String) orderReq.getOrDefault("paymentMethod", "CREDIT_CARD");
            PaymentMethod paymentMethod = PaymentMethod.valueOf(paymentMethodStr);

            List<Map<String, Object>> items = (List<Map<String, Object>>) orderReq.get("items");
            if (items == null || items.isEmpty()) {
                return ResponseEntity.badRequest().body("Cart cannot be empty");
            }

            CartDto cart = new CartDto();
            for (Map<String, Object> itemMap : items) {
                Long prodId = Long.valueOf(itemMap.get("productId").toString());
                int qty = Integer.parseInt(itemMap.get("quantity").toString());
                Optional<Product> pOpt = productService.getProductById(prodId);
                pOpt.ifPresent(p -> cart.addItem(p, qty));
            }

            Order savedOrder = orderService.createOrder(null, name, email, phone, address, city, postalCode, paymentMethod, cart);
            return ResponseEntity.ok(savedOrder);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/orders/{id}/status")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable("id") Long id, @RequestParam("status") OrderStatus status) {
        Order updated = orderService.updateOrderStatus(id, status);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/stats")
    public Map<String, Object> getSystemStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalProducts", productService.countProducts());
        stats.put("totalCategories", categoryService.countCategories());
        stats.put("totalOrders", orderService.countTotalOrders());
        stats.put("totalRevenue", orderService.calculateTotalRevenue());
        return stats;
    }
}
