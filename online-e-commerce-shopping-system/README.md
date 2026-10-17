# Online E-Commerce Shopping System (NexStore)

A modern, full-stack **Online E-Commerce Shopping System** engineered with **Spring Boot**, **Java 21**, **Thymeleaf**, **Spring Data JPA**, and **Bootstrap 5**.

---

## 🌟 Executive Summary & Highlights

The **NexStore E-Commerce Shopping System** provides an end-to-end digital retail platform for both online shoppers and store administrators. The application supports browsing categorized product catalogs, dynamic keyword searching, real-time cart computation, secure multi-method simulated checkout, visual order tracking, printable tax invoices, and an administrative control center for inventory and order management.

- **Zero-Configuration Instant Run:** Uses an embedded H2 database with automatic schema generation and rich pre-seeded catalog data. Also includes ready-to-use MySQL configuration.
- **Full-Stack Architecture:** Spring Boot backend with clean MVC separation, Spring Data JPA persistence, Thymeleaf server-side rendering, and responsive Bootstrap 5 styling.
- **Dual Interface:** Complete web application with server-side rendered UI plus RESTful JSON API endpoints (`/api/*`).

---

## 📋 System Modules & Features

### 1. 🛍️ Customer Storefront & Catalog
- **Interactive Homepage:** Promotional banners, featured product showcase, category cards, and recent arrivals.
- **Product Catalog (`/products`):**
  - Filter products by category (Electronics, Fashion, Home & Kitchen, Books, Beauty).
  - Search by product name, description, or category keyword.
  - Sort by Price (Low to High, High to Low), Customer Rating, or Newest Arrivals.
- **Product Details (`/products/{id}`):**
  - High-resolution imagery, pricing, discounts, stock availability badges, customer review ratings, and specifications.
  - Dynamic quantity selector and related product recommendations from the same category.

### 2. 🛒 Shopping Cart & Checkout
- **Session-Based Cart (`/cart`):**
  - Add to cart with quantity controls.
  - Real-time line item subtotal, 5% estimated tax, and free shipping calculation (free on orders $\ge$ \$50).
  - Badge counter in navigation bar synchronized across all pages.
- **Secure Checkout (`/checkout`):**
  - Customer contact details, shipping address, and city/zip code inputs.
  - Multi-payment options: Credit Card, Debit Card, UPI / Google Pay / PhonePe, Net Banking, and Cash on Delivery (COD).
- **Order Confirmation & Invoicing (`/order-confirmation/{orderNumber}`):**
  - Generated unique order tracking ID (e.g., `ORD-20261010-XXXX`).
  - Itemized official tax invoice with one-click **Print Receipt** (`window.print()`).

### 3. 📦 Order Management & Tracking
- **Order History (`/orders`):**
  - Logged-in customer purchase history.
  - Guest customer order lookup by entering order email.
- **Visual Order Tracker (`/orders/{orderNumber}`):**
  - 4-step interactive delivery progress pipeline: **Order Placed** $\rightarrow$ **Processing** $\rightarrow$ **Dispatched / Shipped** $\rightarrow$ **Delivered**.

### 4. 🛡️ Administrator Control Center (`/admin`)
- **Executive Dashboard (`/admin/dashboard`):**
  - KPI metric cards: **Total Revenue**, **Total Orders**, **Catalog Products**, and **Registered Customers**.
  - **Low Stock Inventory Alerts:** Displays products with stock $< 20$ units with quick restock links.
  - **Recent Orders Feed:** Real-time stream of incoming customer orders.
- **Inventory Management (`/admin/products`):**
  - Add new products with image URLs, descriptions, categories, prices, and stock numbers.
  - Edit existing products and toggle "Featured" status.
  - Delete obsolete products from the catalog.
- **Order Processing (`/admin/orders`):**
  - View all orders across all customers.
  - Update live order delivery status (`PLACED`, `PROCESSING`, `SHIPPED`, `DELIVERED`, `CANCELLED`).
- **Category Management (`/admin/categories`):**
  - Create and manage product categories with Bootstrap icon styling.

### 5. 🔌 RESTful Web API Endpoints
- `GET /api/products` - Returns JSON list of all catalog products.
- `GET /api/products/{id}` - Returns JSON product details by ID.
- `GET /api/categories` - Returns JSON list of all product categories.
- `GET /api/stats` - Returns live system KPI metrics (total products, orders, revenue).

---

## 🏗️ Technology Stack

| Component | Technology | Version |
| :--- | :--- | :--- |
| **Language** | Java | 21 LTS |
| **Framework** | Spring Boot | 4.x / 3.x |
| **Web Layer** | Spring MVC &amp; Tomcat 11 | Embedded |
| **Template Engine** | Thymeleaf | 3.1.5 |
| **ORM / Persistence** | Spring Data JPA / Hibernate | 7.x |
| **Database** | H2 Database (Embedded) / MySQL 8.0 | 2.4.240 / 8.0 |
| **Frontend Styling** | Bootstrap 5, Bootstrap Icons | 5.3.3 / 1.11.3 |
| **Build Tool** | Apache Maven Wrapper | 3.10.0 |

---

## 🗄️ Database Architecture & Data Model

```
+------------------+         +---------------------+         +------------------+
|    categories    | 1     * |      products       | 1     * |   order_items    |
+------------------+---------+---------------------+---------+------------------+
| id (PK)          |         | id (PK)             |         | id (PK)          |
| name             |         | name                |         | order_id (FK)    |
| description      |         | description         |         | product_id (FK)  |
| icon             |         | price               |         | product_name     |
| image_url        |         | original_price      |         | product_price    |
+------------------+         | stock_quantity      |         | quantity         |
                             | image_url           |         | subtotal         |
                             | rating              |         +------------------+
                             | featured            |                   *
                             | category_id (FK)    |                   |
                             +---------------------+                   |
                                                                       | 1
+------------------+ 1     * +---------------------+                   |
|      users       |---------|       orders        |-------------------+
+------------------+         +---------------------+
| id (PK)          |         | id (PK)             |
| name             |         | order_number        |
| email (Unique)   |         | user_id (FK)        |
| password         |         | customer_name       |
| phone            |         | customer_email      |
| address          |         | shipping_address    |
| role             |         | total_amount        |
| created_at       |         | status              |
+------------------+         | payment_method      |
                             | payment_status      |
                             | created_at          |
                             +---------------------+
```

---

## 🚀 How to Run the Application

### 1. Prerequisites
- **Java Development Kit (JDK 21 or higher)**
- No manual database setup required! (H2 runs in-memory automatically).

### 2. Start the Server
Open a terminal in the project directory (`online-e-commerce-shopping-system`) and execute:

```powershell
# On Windows PowerShell / Command Prompt:
.\mvnw.cmd spring-boot:run
```

```bash
# On Linux / macOS:
./mvnw spring-boot:run
```

The application will start at: **`http://localhost:8080`**

---

## 🔑 Demo Login Credentials

The system comes pre-loaded with sample accounts and 1-click login buttons in the navigation bar:

| Role | Email | Password | Access Privileges |
| :--- | :--- | :--- | :--- |
| **Administrator** | `admin@ecommerce.com` | `admin123` | Full Admin Portal, Inventory CRUD, Orders Management |
| **Customer** | `customer@ecommerce.com` | `customer123` | Storefront browsing, Cart, Checkout, Order History |

*(Note: Users can also create new accounts at `/register` or place orders as guests).*

---

## 🔍 Database Console Access

- **URL:** `http://localhost:8080/h2-console`
- **JDBC URL:** `jdbc:h2:mem:ecommercedb`
- **Username:** `sa`
- **Password:** *(leave blank)*

---

## ❓ Academic Presentation & Viva Q&A Guide

**Q1: What is the architecture of this application?**  
*Answer:* The application implements the Model-View-Controller (MVC) architectural pattern. The **Model** consists of JPA entities mapped to database tables; the **Controller** routes HTTP requests and coordinates business logic; the **View** uses Thymeleaf templates styled with Bootstrap 5 to render dynamic HTML.

**Q2: How does the shopping cart maintain state?**  
*Answer:* The cart is maintained in the HTTP Session (`HttpSession`) via `CartDto`. This ensures high performance without unnecessary database writes on every cart increment, allowing seamless shopping for both guests and authenticated users. When an order is placed, cart items are persisted to the database as `Order` and `OrderItem` records.

**Q3: How is inventory concurrency handled?**  
*Answer:* The `OrderService.createOrder` method runs within a `@Transactional` boundary. When an order is placed, stock quantities are validated and decremented. If stock is insufficient, the transaction rolls back safely.

**Q4: How can this application be switched to MySQL?**  
*Answer:* The project already includes `mysql-connector-j`. In `src/main/resources/application.properties`, simply uncomment the MySQL datasource properties and provide the MySQL username/password. Spring Boot will automatically configure Hibernate to connect to MySQL.
