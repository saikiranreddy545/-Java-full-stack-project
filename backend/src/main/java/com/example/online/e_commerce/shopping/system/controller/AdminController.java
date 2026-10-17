package com.example.online.e_commerce.shopping.system.controller;

import com.example.online.e_commerce.shopping.system.model.Category;
import com.example.online.e_commerce.shopping.system.model.OrderStatus;
import com.example.online.e_commerce.shopping.system.model.Product;
import com.example.online.e_commerce.shopping.system.model.User;
import com.example.online.e_commerce.shopping.system.service.CategoryService;
import com.example.online.e_commerce.shopping.system.service.OrderService;
import com.example.online.e_commerce.shopping.system.service.ProductService;
import com.example.online.e_commerce.shopping.system.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final OrderService orderService;
    private final UserService userService;

    public AdminController(ProductService productService,
                           CategoryService categoryService,
                           OrderService orderService,
                           UserService userService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.orderService = orderService;
        this.userService = userService;
    }

    private boolean checkAdmin(HttpSession session) {
        User user = (User) session.getAttribute(GlobalControllerAdvice.SESSION_USER_KEY);
        return user != null && user.isAdmin();
    }

    @GetMapping({"", "/dashboard"})
    public String dashboard(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        if (!checkAdmin(session)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Admin access required. Please sign in as administrator.");
            return "redirect:/login";
        }

        model.addAttribute("totalRevenue", orderService.calculateTotalRevenue());
        model.addAttribute("totalOrders", orderService.countTotalOrders());
        model.addAttribute("totalProducts", productService.countProducts());
        model.addAttribute("totalUsers", userService.countUsers());
        model.addAttribute("pendingOrders", orderService.countOrdersByStatus(OrderStatus.PLACED));
        model.addAttribute("recentOrders", orderService.getRecentOrders());
        model.addAttribute("lowStockProducts", productService.getLowStockProducts(20));

        return "admin/dashboard";
    }

    // --- PRODUCT MANAGEMENT ---

    @GetMapping("/products")
    public String listProducts(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        if (!checkAdmin(session)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Admin access required.");
            return "redirect:/login";
        }
        model.addAttribute("products", productService.getAllProducts());
        return "admin/products";
    }

    @GetMapping("/products/new")
    public String newProductForm(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        if (!checkAdmin(session)) {
            return "redirect:/login";
        }
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "admin/product-form";
    }

    @GetMapping("/products/edit/{id}")
    public String editProductForm(@PathVariable("id") Long id, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        if (!checkAdmin(session)) {
            return "redirect:/login";
        }
        Optional<Product> prodOpt = productService.getProductById(id);
        if (prodOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Product not found.");
            return "redirect:/admin/products";
        }
        model.addAttribute("product", prodOpt.get());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "admin/product-form";
    }

    @PostMapping("/products/save")
    public String saveProduct(@ModelAttribute("product") Product product,
                              @RequestParam("categoryId") Long categoryId,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        if (!checkAdmin(session)) {
            return "redirect:/login";
        }

        categoryService.getCategoryById(categoryId).ifPresent(product::setCategory);
        productService.saveProduct(product);
        redirectAttributes.addFlashAttribute("successMessage", "Product saved successfully!");
        return "redirect:/admin/products";
    }

    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        if (!checkAdmin(session)) {
            return "redirect:/login";
        }
        productService.deleteProduct(id);
        redirectAttributes.addFlashAttribute("successMessage", "Product deleted successfully!");
        return "redirect:/admin/products";
    }

    // --- ORDER MANAGEMENT ---

    @GetMapping("/orders")
    public String listOrders(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        if (!checkAdmin(session)) {
            return "redirect:/login";
        }
        model.addAttribute("orders", orderService.getAllOrders());
        model.addAttribute("allStatuses", OrderStatus.values());
        return "admin/orders";
    }

    @PostMapping("/orders/{id}/status")
    public String updateOrderStatus(@PathVariable("id") Long id,
                                    @RequestParam("status") OrderStatus status,
                                    HttpSession session,
                                    RedirectAttributes redirectAttributes) {
        if (!checkAdmin(session)) {
            return "redirect:/login";
        }
        orderService.updateOrderStatus(id, status);
        redirectAttributes.addFlashAttribute("successMessage", "Order status updated to " + status.getDisplayName() + "!");
        return "redirect:/admin/orders";
    }

    // --- CATEGORY MANAGEMENT ---

    @GetMapping("/categories")
    public String listCategories(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        if (!checkAdmin(session)) {
            return "redirect:/login";
        }
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("newCategory", new Category());
        return "admin/categories";
    }

    @PostMapping("/categories/save")
    public String saveCategory(@ModelAttribute("newCategory") Category category,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        if (!checkAdmin(session)) {
            return "redirect:/login";
        }
        categoryService.saveCategory(category);
        redirectAttributes.addFlashAttribute("successMessage", "Category saved successfully!");
        return "redirect:/admin/categories";
    }

    @GetMapping("/categories/delete/{id}")
    public String deleteCategory(@PathVariable("id") Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        if (!checkAdmin(session)) {
            return "redirect:/login";
        }
        try {
            categoryService.deleteCategory(id);
            redirectAttributes.addFlashAttribute("successMessage", "Category deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Cannot delete category that contains products.");
        }
        return "redirect:/admin/categories";
    }
}
