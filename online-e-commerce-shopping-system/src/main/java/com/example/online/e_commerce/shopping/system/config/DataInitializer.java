package com.example.online.e_commerce.shopping.system.config;

import com.example.online.e_commerce.shopping.system.model.*;
import com.example.online.e_commerce.shopping.system.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public DataInitializer(UserRepository userRepository,
                           CategoryRepository categoryRepository,
                           ProductRepository productRepository,
                           OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public void run(String... args) {
        if (categoryRepository.count() > 0) {
            return; // Data already initialized
        }

        // 1. Initialize Users
        User admin = new User(
                "System Administrator",
                "admin@ecommerce.com",
                "admin123",
                "+1-800-555-0199",
                "100 Tech Blvd, Silicon Valley, CA",
                Role.ROLE_ADMIN
        );
        userRepository.save(admin);

        User customer = new User(
                "Alex Johnson",
                "customer@ecommerce.com",
                "customer123",
                "+1-800-555-0144",
                "742 Evergreen Terrace, Springfield, OR",
                Role.ROLE_CUSTOMER
        );
        userRepository.save(customer);

        // 2. Initialize Categories
        Category catElectronics = new Category(
                "Electronics & Gadgets",
                "Laptops, smartphones, audio devices, and smart accessories",
                "bi-laptop",
                "https://images.unsplash.com/photo-1498049794561-7780e7231661?w=600&auto=format&fit=crop&q=80"
        );
        Category catFashion = new Category(
                "Fashion & Apparel",
                "Trendy clothing, footwear, watches, and designer accessories",
                "bi-bag-check",
                "https://images.unsplash.com/photo-1445205170230-053b83016050?w=600&auto=format&fit=crop&q=80"
        );
        Category catHome = new Category(
                "Home & Kitchen",
                "Modern furniture, kitchenware, decor, and smart home essentials",
                "bi-house-door",
                "https://images.unsplash.com/photo-1556911220-e15b29be8c8f?w=600&auto=format&fit=crop&q=80"
        );
        Category catBooks = new Category(
                "Books & Stationery",
                "Best-selling books, academic guides, journals, and stationery",
                "bi-book",
                "https://images.unsplash.com/photo-1512820790803-83ca734da794?w=600&auto=format&fit=crop&q=80"
        );
        Category catBeauty = new Category(
                "Beauty & Personal Care",
                "Skincare, grooming, wellness essentials, and fragrances",
                "bi-heart-pulse",
                "https://images.unsplash.com/photo-1522335789203-aabd1fc54bc9?w=600&auto=format&fit=crop&q=80"
        );

        categoryRepository.saveAll(Arrays.asList(catElectronics, catFashion, catHome, catBooks, catBeauty));

        // 3. Initialize Products
        List<Product> products = Arrays.asList(
                // Electronics
                new Product(
                        "UltraPro Noise-Canceling Wireless Headphones",
                        "Industry-leading active noise cancelation with crystal-clear 40mm audio drivers and 35-hour battery life. Features fast USB-C charging and seamless Bluetooth 5.3 multipoint connectivity.",
                        199.99,
                        249.99,
                        45,
                        "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=600&auto=format&fit=crop&q=80",
                        4.8,
                        128,
                        true,
                        catElectronics
                ),
                new Product(
                        "Titanium Smart Watch Series 8",
                        "Water-resistant titanium smartwatch with always-on AMOLED display, comprehensive heart rate & ECG tracking, sleep analysis, and integrated GPS.",
                        249.50,
                        299.99,
                        30,
                        "https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=600&auto=format&fit=crop&q=80",
                        4.7,
                        89,
                        true,
                        catElectronics
                ),
                new Product(
                        "AeroBook 15 Pro Laptop (16GB RAM / 512GB SSD)",
                        "Ultra-thin lightweight aluminum chassis powered by an octa-core high-performance processor, stunning 2.8K OLED display, and all-day battery life.",
                        899.00,
                        1099.00,
                        18,
                        "https://images.unsplash.com/photo-1496181133206-80ce9b88a853?w=600&auto=format&fit=crop&q=80",
                        4.9,
                        64,
                        true,
                        catElectronics
                ),
                new Product(
                        "4K Ultra-HD Action Camera with Gimbal Stabilizer",
                        "Capture breathtaking 4K 60FPS video with dual color screens, waterproof design down to 10 meters, and advanced EIS 6-axis electronic image stabilization.",
                        149.00,
                        179.99,
                        25,
                        "https://images.unsplash.com/photo-1526170375885-4d8ecf77b99f?w=600&auto=format&fit=crop&q=80",
                        4.6,
                        42,
                        false,
                        catElectronics
                ),

                // Fashion
                new Product(
                        "Classic Minimalist Chronograph Wristwatch",
                        "Timeless handcrafted analog watch with genuine Italian leather strap, sapphire crystal glass, and Japanese quartz movement.",
                        129.00,
                        159.00,
                        40,
                        "https://images.unsplash.com/photo-1524805444758-089113d48a6d?w=600&auto=format&fit=crop&q=80",
                        4.7,
                        76,
                        true,
                        catFashion
                ),
                new Product(
                        "Heritage Leather Weekend Duffel Bag",
                        "Crafted from premium full-grain buffalo leather with durable brass zippers and reinforced water-resistant canvas lining. Perfect for travel or gym.",
                        89.99,
                        120.00,
                        22,
                        "https://images.unsplash.com/photo-1553062407-98eeb64c6a62?w=600&auto=format&fit=crop&q=80",
                        4.8,
                        53,
                        true,
                        catFashion
                ),
                new Product(
                        "Urban Streetwear Polarized Sunglasses",
                        "Lightweight acetate frame with UV400 anti-glare polarized lenses designed for maximum comfort and crisp optic clarity.",
                        45.00,
                        65.00,
                        60,
                        "https://images.unsplash.com/photo-1511499767150-a48a237f0083?w=600&auto=format&fit=crop&q=80",
                        4.5,
                        91,
                        false,
                        catFashion
                ),
                new Product(
                        "SportFlow Breathable Cushion Running Shoes",
                        "Engineered mesh upper with responsive energy-return foam midsole for unmatched athletic performance and cloud-like daily walking comfort.",
                        79.99,
                        99.99,
                        35,
                        "https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=600&auto=format&fit=crop&q=80",
                        4.6,
                        110,
                        false,
                        catFashion
                ),

                // Home & Kitchen
                new Product(
                        "Barista Express Programmable Espresso Machine",
                        "15-bar Italian pump with integrated conical burr grinder, precise digital PID temperature control, and micro-foam steam wand for café-quality latte art.",
                        299.00,
                        349.99,
                        15,
                        "https://images.unsplash.com/photo-1517668808822-9ebb02f2a0e6?w=600&auto=format&fit=crop&q=80",
                        4.9,
                        84,
                        true,
                        catHome
                ),
                new Product(
                        "Nordic Ceramic Pour-Over Coffee Carafe & Mug Set",
                        "Artisan matte ceramic kettle with stainless steel permanent mesh filter and pair of double-walled heat-resistant ceramic mugs.",
                        39.99,
                        49.99,
                        50,
                        "https://images.unsplash.com/photo-1514432324607-a09d9b4aefdd?w=600&auto=format&fit=crop&q=80",
                        4.7,
                        38,
                        false,
                        catHome
                ),
                new Product(
                        "Aroma Ultrasonic Essential Oil Diffuser",
                        "Whisper-quiet 500ml capacity cold mist humidifier with 7 ambient LED mood lighting colors and auto-shutoff timer.",
                        28.50,
                        35.00,
                        70,
                        "https://images.unsplash.com/photo-1608571423902-eed4a5ad8108?w=600&auto=format&fit=crop&q=80",
                        4.4,
                        65,
                        false,
                        catHome
                ),

                // Books & Stationery
                new Product(
                        "Clean Code & Architecture Engineering Handbook",
                        "Essential best practices guide for professional software engineering, domain-driven design principles, and enterprise design patterns.",
                        42.00,
                        50.00,
                        80,
                        "https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?w=600&auto=format&fit=crop&q=80",
                        4.9,
                        210,
                        true,
                        catBooks
                ),
                new Product(
                        "Executive Hardcover Dotted Grid Journal with Fountain Pen",
                        "Archival 120gsm ink-proof bleed-resistant ivory paper with magnetic closure and balanced precision brass fountain pen.",
                        24.99,
                        32.00,
                        65,
                        "https://images.unsplash.com/photo-1544947950-fa07a98d237f?w=600&auto=format&fit=crop&q=80",
                        4.8,
                        47,
                        false,
                        catBooks
                ),

                // Beauty & Wellness
                new Product(
                        "Botanical Organic Hydrating Facial Serum (50ml)",
                        "Formulated with natural botanical hyaluronic acid, vitamin C, and cold-pressed jojoba extract to replenish moisture and restore glowing skin.",
                        34.00,
                        45.00,
                        55,
                        "https://images.unsplash.com/photo-1620916566398-39f1143ab7be?w=600&auto=format&fit=crop&q=80",
                        4.7,
                        93,
                        true,
                        catBeauty
                ),
                new Product(
                        "Pure Sandalwood & Cedarwood Eau de Parfum (100ml)",
                        "Sophisticated woody fragrance crafted from sustainably sourced sandalwood, amber resins, and refreshing bergamot notes.",
                        65.00,
                        80.00,
                        40,
                        "https://images.unsplash.com/photo-1592945403244-b3fbafd7f539?w=600&auto=format&fit=crop&q=80",
                        4.6,
                        58,
                        false,
                        catBeauty
                )
        );

        productRepository.saveAll(products);

        // 4. Initialize Sample Demo Order
        Order sampleOrder = new Order(
                "ORD-20261010-DEMO01",
                customer,
                customer.getName(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getAddress(),
                "Springfield",
                "97477",
                449.49,
                OrderStatus.PROCESSING,
                PaymentMethod.CREDIT_CARD,
                "PAID"
        );

        Product p1 = products.get(0); // Headphones
        Product p2 = products.get(1); // Smartwatch
        sampleOrder.addItem(new OrderItem(sampleOrder, p1, p1.getName(), p1.getPrice(), 1));
        sampleOrder.addItem(new OrderItem(sampleOrder, p2, p2.getName(), p2.getPrice(), 1));

        orderRepository.save(sampleOrder);
    }
}
