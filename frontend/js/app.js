const API_BASE = 'http://localhost:8080/api';

// Fallback sample catalog if backend API is not running
const DEFAULT_PRODUCTS = [
    {
        id: 1,
        name: "UltraPro Noise-Canceling Wireless Headphones",
        description: "Industry-leading active noise cancelation with crystal-clear 40mm audio drivers and 35-hour battery life.",
        price: 199.99,
        originalPrice: 249.99,
        stockQuantity: 45,
        rating: 4.8,
        reviewCount: 128,
        imageUrl: "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=600&auto=format&fit=crop&q=80",
        category: { id: 1, name: "Electronics & Gadgets" }
    },
    {
        id: 2,
        name: "Titanium Smart Watch Series 8",
        description: "Water-resistant titanium smartwatch with always-on AMOLED display, heart rate & ECG tracking.",
        price: 249.50,
        originalPrice: 299.99,
        stockQuantity: 30,
        rating: 4.7,
        reviewCount: 89,
        imageUrl: "https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=600&auto=format&fit=crop&q=80",
        category: { id: 1, name: "Electronics & Gadgets" }
    },
    {
        id: 3,
        name: "AeroBook 15 Pro Laptop (16GB RAM / 512GB SSD)",
        description: "Ultra-thin lightweight aluminum chassis powered by octa-core processor and 2.8K OLED display.",
        price: 899.00,
        originalPrice: 1099.00,
        stockQuantity: 18,
        rating: 4.9,
        reviewCount: 64,
        imageUrl: "https://images.unsplash.com/photo-1496181133206-80ce9b88a853?w=600&auto=format&fit=crop&q=80",
        category: { id: 1, name: "Electronics & Gadgets" }
    },
    {
        id: 4,
        name: "Classic Minimalist Chronograph Wristwatch",
        description: "Handcrafted analog watch with genuine Italian leather strap and sapphire crystal glass.",
        price: 129.00,
        originalPrice: 159.00,
        stockQuantity: 40,
        rating: 4.7,
        reviewCount: 76,
        imageUrl: "https://images.unsplash.com/photo-1524805444758-089113d48a6d?w=600&auto=format&fit=crop&q=80",
        category: { id: 2, name: "Fashion & Apparel" }
    },
    {
        id: 5,
        name: "Heritage Leather Weekend Duffel Bag",
        description: "Crafted from premium full-grain buffalo leather with durable brass zippers.",
        price: 89.99,
        originalPrice: 120.00,
        stockQuantity: 22,
        rating: 4.8,
        reviewCount: 53,
        imageUrl: "https://images.unsplash.com/photo-1553062407-98eeb64c6a62?w=600&auto=format&fit=crop&q=80",
        category: { id: 2, name: "Fashion & Apparel" }
    },
    {
        id: 6,
        name: "Barista Express Programmable Espresso Machine",
        description: "15-bar Italian pump with conical burr grinder and precise digital temperature control.",
        price: 299.00,
        originalPrice: 349.99,
        stockQuantity: 15,
        rating: 4.9,
        reviewCount: 84,
        imageUrl: "https://images.unsplash.com/photo-1517668808822-9ebb02f2a0e6?w=600&auto=format&fit=crop&q=80",
        category: { id: 3, name: "Home & Kitchen" }
    },
    {
        id: 7,
        name: "Clean Code & Architecture Engineering Handbook",
        description: "Essential best practices guide for professional software engineering and clean system design.",
        price: 42.00,
        originalPrice: 50.00,
        stockQuantity: 80,
        rating: 4.9,
        reviewCount: 210,
        imageUrl: "https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?w=600&auto=format&fit=crop&q=80",
        category: { id: 4, name: "Books & Stationery" }
    },
    {
        id: 8,
        name: "Botanical Organic Hydrating Facial Serum (50ml)",
        description: "Formulated with hyaluronic acid, vitamin C, and cold-pressed botanical jojoba extract.",
        price: 34.00,
        originalPrice: 45.00,
        stockQuantity: 55,
        rating: 4.7,
        reviewCount: 93,
        imageUrl: "https://images.unsplash.com/photo-1620916566398-39f1143ab7be?w=600&auto=format&fit=crop&q=80",
        category: { id: 5, name: "Beauty & Wellness" }
    }
];

let products = [];
let cart = [];
let orders = [
    {
        orderNumber: "ORD-20261010-DEMO01",
        customerName: "Alex Johnson",
        customerEmail: "alex@example.com",
        customerPhone: "+1 555-0144",
        shippingAddress: "742 Evergreen Terrace",
        city: "Springfield",
        postalCode: "97477",
        totalAmount: 449.49,
        status: "PROCESSING",
        paymentMethod: "Credit Card",
        createdAt: "2026-10-10 14:30",
        items: [
            { productName: "UltraPro Noise-Canceling Wireless Headphones", productPrice: 199.99, quantity: 1, subtotal: 199.99 },
            { productName: "Titanium Smart Watch Series 8", productPrice: 249.50, quantity: 1, subtotal: 249.50 }
        ]
    }
];

let selectedCategory = 'All';

// Initialize
document.addEventListener('DOMContentLoaded', () => {
    fetchProducts();
    updateCartUI();
});

async function fetchProducts() {
    try {
        const res = await fetch(`${API_BASE}/products`);
        if (res.ok) {
            const data = await res.json();
            if (data && data.length > 0) {
                products = data;
            } else {
                products = DEFAULT_PRODUCTS;
            }
        } else {
            products = DEFAULT_PRODUCTS;
        }
    } catch (e) {
        products = DEFAULT_PRODUCTS;
    }
    renderProducts();
    renderAdminProducts();
    fetchStats();
}

async function fetchStats() {
    try {
        const res = await fetch(`${API_BASE}/stats`);
        if (res.ok) {
            const stats = await res.json();
            document.getElementById('adminRevenue').innerText = `$${Number(stats.totalRevenue).toFixed(2)}`;
            document.getElementById('adminOrdersCount').innerText = stats.totalOrders;
            document.getElementById('adminProductsCount').innerText = stats.totalProducts;
        }
    } catch (e) {
        // Fallback stats
    }
}

function renderProducts() {
    const grid = document.getElementById('productGrid');
    const query = document.getElementById('searchInput').value.toLowerCase().trim();
    
    let filtered = products.filter(p => {
        const matchesCategory = selectedCategory === 'All' || 
            (p.category && p.category.name && p.category.name.toLowerCase().includes(selectedCategory.toLowerCase()));
        const matchesQuery = !query || p.name.toLowerCase().includes(query) || (p.description && p.description.toLowerCase().includes(query));
        return matchesCategory && matchesQuery;
    });

    document.getElementById('productCountText').innerText = `Showing ${filtered.length} products`;

    if (filtered.length === 0) {
        grid.innerHTML = `
            <div class="col-12 text-center py-5">
                <i class="bi bi-search fs-1 text-muted d-block mb-3"></i>
                <h5 class="fw-bold">No Products Found</h5>
                <p class="text-muted">Try adjusting your category filter or search keywords.</p>
            </div>
        `;
        return;
    }

    grid.innerHTML = filtered.map(prod => `
        <div class="col">
            <div class="product-card">
                <div class="product-card-img-wrapper">
                    <img src="${prod.imageUrl}" alt="${prod.name}" class="product-card-img">
                    ${prod.originalPrice && prod.originalPrice > prod.price ? `<span class="badge-discount">${Math.round(((prod.originalPrice - prod.price) / prod.originalPrice) * 100)}% OFF</span>` : ''}
                    <span class="badge bg-success badge-stock">In Stock</span>
                </div>
                <div class="p-3 d-flex flex-column flex-grow-1">
                    <small class="text-uppercase text-muted fw-bold mb-1">${prod.category ? prod.category.name : 'Store'}</small>
                    <h6 class="fw-bold mb-2 text-truncate">${prod.name}</h6>
                    <div class="d-flex align-items-center mb-2 small">
                        <span class="text-warning me-1"><i class="bi bi-star-fill"></i></span>
                        <span class="fw-bold me-1">${prod.rating || 4.8}</span>
                        <span class="text-muted">(${prod.reviewCount || 42})</span>
                    </div>
                    <div class="mt-auto d-flex align-items-baseline gap-2 pt-2">
                        <span class="fs-5 fw-bold text-primary">$${Number(prod.price).toFixed(2)}</span>
                        ${prod.originalPrice ? `<span class="text-decoration-line-through text-muted small">$${Number(prod.originalPrice).toFixed(2)}</span>` : ''}
                    </div>
                    <button class="btn btn-primary btn-sm rounded-pill w-100 mt-3 fw-bold" onclick="addToCart(${prod.id})">
                        <i class="bi bi-cart-plus me-1"></i> Add to Cart
                    </button>
                </div>
            </div>
        </div>
    `).join('');
}

function filterCategory(cat, btn) {
    selectedCategory = cat;
    document.querySelectorAll('.category-pill').forEach(el => el.classList.remove('active'));
    if (btn) btn.classList.add('active');
    renderProducts();
}

function filterProducts() {
    renderProducts();
}

function sortProducts() {
    const val = document.getElementById('sortSelect').value;
    if (val === 'price_asc') {
        products.sort((a, b) => a.price - b.price);
    } else if (val === 'price_desc') {
        products.sort((a, b) => b.price - a.price);
    } else if (val === 'rating') {
        products.sort((a, b) => (b.rating || 0) - (a.rating || 0));
    }
    renderProducts();
}

// CART MANAGEMENT
function addToCart(productId) {
    const prod = products.find(p => p.id === productId);
    if (!prod) return;

    const existing = cart.find(item => item.product.id === productId);
    if (existing) {
        existing.quantity += 1;
    } else {
        cart.push({ product: prod, quantity: 1 });
    }
    updateCartUI();

    // Show toast or open cart
    const offcanvasEl = document.getElementById('cartOffcanvas');
    const bsOffcanvas = bootstrap.Offcanvas.getOrCreateInstance(offcanvasEl);
    bsOffcanvas.show();
}

function updateCartQty(productId, delta) {
    const item = cart.find(i => i.product.id === productId);
    if (!item) return;
    item.quantity += delta;
    if (item.quantity <= 0) {
        cart = cart.filter(i => i.product.id !== productId);
    }
    updateCartUI();
}

function removeFromCart(productId) {
    cart = cart.filter(i => i.product.id !== productId);
    updateCartUI();
}

function updateCartUI() {
    const totalQty = cart.reduce((sum, item) => sum + item.quantity, 0);
    document.getElementById('cartBadge').innerText = totalQty;

    const list = document.getElementById('cartItemsList');
    if (cart.length === 0) {
        list.innerHTML = `
            <div class="text-center py-5 text-muted">
                <i class="bi bi-cart-x fs-1 d-block mb-2"></i>
                <p>Your shopping cart is currently empty.</p>
            </div>
        `;
        document.getElementById('cartSubtotal').innerText = '$0.00';
        document.getElementById('cartTax').innerText = '$0.00';
        document.getElementById('cartShipping').innerText = 'FREE';
        document.getElementById('cartTotal').innerText = '$0.00';
        document.getElementById('checkoutBtn').disabled = true;
        return;
    }

    document.getElementById('checkoutBtn').disabled = false;
    let subtotal = 0;

    list.innerHTML = cart.map(item => {
        const itemTotal = item.product.price * item.quantity;
        subtotal += itemTotal;
        return `
            <div class="d-flex align-items-center gap-3 py-2 border-bottom">
                <img src="${item.product.imageUrl}" class="rounded-3 object-fit-cover" style="width: 50px; height: 50px;">
                <div class="flex-grow-1 text-truncate">
                    <h6 class="mb-0 small fw-bold text-truncate">${item.product.name}</h6>
                    <small class="text-muted">$${Number(item.product.price).toFixed(2)}</small>
                </div>
                <div class="d-flex align-items-center gap-1">
                    <button class="btn btn-sm btn-light border p-1 rounded-circle" style="width: 24px; height: 24px; line-height: 1;" onclick="updateCartQty(${item.product.id}, -1)">-</button>
                    <span class="small fw-bold px-1">${item.quantity}</span>
                    <button class="btn btn-sm btn-light border p-1 rounded-circle" style="width: 24px; height: 24px; line-height: 1;" onclick="updateCartQty(${item.product.id}, 1)">+</button>
                </div>
                <div class="small fw-bold text-primary">$${itemTotal.toFixed(2)}</div>
                <button class="btn btn-sm text-danger p-0" onclick="removeFromCart(${item.product.id})"><i class="bi bi-x"></i></button>
            </div>
        `;
    }).join('');

    const tax = Math.round((subtotal * 0.05) * 100) / 100;
    const shipping = subtotal >= 50.0 || subtotal === 0 ? 0.0 : 5.0;
    const total = subtotal + tax + shipping;

    document.getElementById('cartSubtotal').innerText = `$${subtotal.toFixed(2)}`;
    document.getElementById('cartTax').innerText = `$${tax.toFixed(2)}`;
    document.getElementById('cartShipping').innerText = shipping === 0 ? 'FREE' : `$${shipping.toFixed(2)}`;
    document.getElementById('cartTotal').innerText = `$${total.toFixed(2)}`;
    document.getElementById('modalPayTotal').innerText = `$${total.toFixed(2)}`;
}

function openCheckout() {
    const offcanvasEl = document.getElementById('cartOffcanvas');
    bootstrap.Offcanvas.getInstance(offcanvasEl).hide();
    const modalEl = document.getElementById('checkoutModal');
    new bootstrap.Modal(modalEl).show();
}

async function handleCheckoutSubmit(e) {
    e.preventDefault();
    const totalStr = document.getElementById('cartTotal').innerText.replace('$', '');
    const totalAmount = parseFloat(totalStr);

    const newOrder = {
        orderNumber: "ORD-" + new Date().toISOString().slice(0,10).replace(/-/g,'') + "-" + Math.random().toString(36).substring(2,7).toUpperCase(),
        customerName: document.getElementById('chkName').value,
        customerEmail: document.getElementById('chkEmail').value,
        customerPhone: document.getElementById('chkPhone').value,
        shippingAddress: document.getElementById('chkAddress').value,
        city: document.getElementById('chkCity').value,
        postalCode: document.getElementById('chkZip').value,
        totalAmount: totalAmount,
        status: "PLACED",
        paymentMethod: document.getElementById('chkPayment').value.replace('_', ' '),
        createdAt: new Date().toLocaleString(),
        items: cart.map(i => ({
            productName: i.product.name,
            productPrice: i.product.price,
            quantity: i.quantity,
            subtotal: i.product.price * i.quantity
        }))
    };

    // Attempt to post to backend API
    try {
        await fetch(`${API_BASE}/orders`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                customerName: newOrder.customerName,
                customerEmail: newOrder.customerEmail,
                customerPhone: newOrder.customerPhone,
                shippingAddress: newOrder.shippingAddress,
                city: newOrder.city,
                postalCode: newOrder.postalCode,
                paymentMethod: document.getElementById('chkPayment').value,
                items: cart.map(i => ({ productId: i.product.id, quantity: i.quantity }))
            })
        });
    } catch(err) {
        // Local simulation fallback
    }

    orders.unshift(newOrder);

    // Hide checkout modal
    bootstrap.Modal.getInstance(document.getElementById('checkoutModal')).hide();

    // Clear cart
    cart = [];
    updateCartUI();

    // Show Printable Receipt Modal
    showReceiptModal(newOrder);
}

function showReceiptModal(ord) {
    const modalEl = document.getElementById('receiptModal');
    const content = document.getElementById('receiptContent');

    content.innerHTML = `
        <div class="bg-success text-white rounded-circle d-inline-flex p-3 mb-3 shadow">
            <i class="bi bi-check2 fs-1"></i>
        </div>
        <h3 class="fw-bold mb-1">Thank You For Your Order!</h3>
        <p class="text-muted">Order <strong class="text-primary">${ord.orderNumber}</strong> has been placed successfully.</p>
        
        <div class="card bg-light border text-start p-4 rounded-4 my-4">
            <div class="d-flex justify-content-between mb-3 border-bottom pb-2">
                <span class="fw-bold">NexStore Official Invoice</span>
                <span class="badge bg-success">Status: Confirmed</span>
            </div>
            <div class="row small mb-3">
                <div class="col-6">
                    <strong>Recipient:</strong> ${ord.customerName}<br>
                    <strong>Email:</strong> ${ord.customerEmail}<br>
                    <strong>Phone:</strong> ${ord.customerPhone}
                </div>
                <div class="col-6 text-end">
                    <strong>Delivery Address:</strong><br>
                    ${ord.shippingAddress}, ${ord.city} ${ord.postalCode}<br>
                    <strong>Payment:</strong> ${ord.paymentMethod} (PAID)
                </div>
            </div>
            <table class="table table-sm table-bordered bg-white mb-2">
                <thead><tr><th>Item</th><th class="text-center">Qty</th><th class="text-end">Subtotal</th></tr></thead>
                <tbody>
                    ${ord.items.map(i => `<tr><td>${i.productName}</td><td class="text-center">${i.quantity}</td><td class="text-end">$${Number(i.subtotal).toFixed(2)}</td></tr>`).join('')}
                </tbody>
            </table>
            <div class="text-end fs-5 fw-bold text-primary">
                Total Paid: $${Number(ord.totalAmount).toFixed(2)}
            </div>
        </div>
    `;

    new bootstrap.Modal(modalEl).show();
}

// SECTION SWITCHER
function showSection(sec) {
    document.getElementById('section-shop').classList.add('d-none');
    document.getElementById('section-orders').classList.add('d-none');
    document.getElementById('section-admin').classList.add('d-none');

    document.getElementById('nav-shop').classList.remove('active');
    document.getElementById('nav-orders').classList.remove('active');
    document.getElementById('nav-admin').classList.remove('active');

    if (sec === 'shop') {
        document.getElementById('section-shop').classList.remove('d-none');
        document.getElementById('nav-shop').classList.add('active');
    } else if (sec === 'orders') {
        document.getElementById('section-orders').classList.remove('d-none');
        document.getElementById('nav-orders').classList.add('active');
        renderOrdersList();
    } else if (sec === 'admin') {
        document.getElementById('section-admin').classList.remove('d-none');
        document.getElementById('nav-admin').classList.add('active');
        renderAdminProducts();
        fetchStats();
    }
}

function renderOrdersList() {
    const container = document.getElementById('ordersList');
    if (orders.length === 0) {
        container.innerHTML = `<div class="p-5 text-center text-muted">No orders found.</div>`;
        return;
    }

    container.innerHTML = `
        <table class="table align-middle mb-0">
            <thead class="table-light">
                <tr>
                    <th class="ps-4">Order #</th>
                    <th>Customer</th>
                    <th>Items</th>
                    <th>Total</th>
                    <th>Payment</th>
                    <th>Status</th>
                </tr>
            </thead>
            <tbody>
                ${orders.map(o => `
                    <tr>
                        <td class="ps-4 fw-bold text-primary">${o.orderNumber}<br><small class="text-muted fw-normal">${o.createdAt}</small></td>
                        <td>${o.customerName}<br><small class="text-muted">${o.customerEmail}</small></td>
                        <td><span class="badge bg-light text-dark border">${o.items.length} items</span></td>
                        <td class="fw-bold">$${Number(o.totalAmount).toFixed(2)}</td>
                        <td><span class="badge bg-success-subtle text-success">${o.paymentMethod}</span></td>
                        <td><span class="badge bg-primary px-3 py-2 rounded-pill">${o.status}</span></td>
                    </tr>
                `).join('')}
            </tbody>
        </table>
    `;
}

function renderAdminProducts() {
    const table = document.getElementById('adminProductsTable');
    table.innerHTML = products.map(p => `
        <tr>
            <td class="ps-4 py-3">
                <div class="d-flex align-items-center gap-3">
                    <img src="${p.imageUrl}" class="rounded-3 object-fit-cover shadow-sm" style="width: 45px; height: 45px;">
                    <div><strong class="d-block text-truncate" style="max-width: 250px;">${p.name}</strong></div>
                </div>
            </td>
            <td><span class="badge bg-light text-dark border">${p.category ? p.category.name : 'Category'}</span></td>
            <td class="fw-bold">$${Number(p.price).toFixed(2)}</td>
            <td><span class="badge bg-success-subtle text-success">${p.stockQuantity || 25} in stock</span></td>
            <td class="text-end pe-4">
                <button class="btn btn-sm btn-outline-danger rounded-pill" onclick="deleteProduct(${p.id})">
                    <i class="bi bi-trash"></i> Delete
                </button>
            </td>
        </tr>
    `).join('');
}

function handleAddProductSubmit(e) {
    e.preventDefault();
    const newP = {
        id: Date.now(),
        name: document.getElementById('addProdName').value,
        description: document.getElementById('addProdDesc').value,
        price: parseFloat(document.getElementById('addProdPrice').value),
        originalPrice: parseFloat(document.getElementById('addProdPrice').value) * 1.2,
        stockQuantity: parseInt(document.getElementById('addProdStock').value),
        imageUrl: document.getElementById('addProdImg').value,
        rating: 4.8,
        reviewCount: 15,
        category: { id: parseInt(document.getElementById('addProdCategory').value), name: document.getElementById('addProdCategory').options[document.getElementById('addProdCategory').selectedIndex].text }
    };

    products.unshift(newP);
    renderProducts();
    renderAdminProducts();

    bootstrap.Modal.getInstance(document.getElementById('addProductModal')).hide();
    alert('Product added successfully to catalog!');
}

function deleteProduct(id) {
    if (confirm('Are you sure you want to delete this product?')) {
        products = products.filter(p => p.id !== id);
        renderProducts();
        renderAdminProducts();
    }
}
