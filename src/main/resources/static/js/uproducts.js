// =====================================
// API URLS
// =====================================

const API = "/api/products";
const CART_API = "/api/cart";
const WISHLIST_API = "/api/wishlist";


// =====================================
// VARIABLES
// =====================================

let currentPage = 0;
let totalPages = 0;

const pageSize = 12;

let selectedCategoryId = null;


// =====================================
// PAGE LOAD
// =====================================

document.addEventListener("DOMContentLoaded", () => {

    loadProducts();

});


// =====================================
// LOAD ALL PRODUCTS
// =====================================

function loadProducts() {

    const sort =
        document.getElementById("priceRange")?.value || "";

    let url =
        `${API}?page=${currentPage}&size=${pageSize}`;

    // Backend expects:
    // sort=asc
    // sort=desc

    if (sort !== "") {

        url += `&sort=${sort}`;

    }

    console.log("LOAD PRODUCTS URL :", url);

    fetch(url)

        .then(res => {

            if (!res.ok) {

                throw new Error(
                    "Unable to load products"
                );

            }

            return res.json();

        })

        .then(data => {

            console.log("PRODUCT DATA :", data);

            totalPages = data.totalPages;

            displayProducts(data.content);

            updatePagination();

        })

        .catch(err => {

            console.error(err);

        });

}


// =====================================
// DISPLAY PRODUCTS
// =====================================

function displayProducts(products) {

    const container =
        document.getElementById("productContainer");

    if (!container) {
        return;
    }

    container.innerHTML = "";

    if (!products || products.length === 0) {

        container.innerHTML = `
            <h2 class="no-product">
                No Products Available
            </h2>
        `;

        return;
    }


    products.forEach(product => {

        const image =
            product.image
                ? product.image
                : "/images/no-image.png";


        const category =
            product.category
                ? product.category.name
                : "N/A";


        container.innerHTML += `

            <div class="card">

                <div class="image-box">

                    <img
                        src="${image}"
                        onerror="this.src='/images/no-image.png'"
                    >

                </div>


                <h3>
                    ${product.name}
                </h3>


                <p>
                    <strong>Brand :</strong>
                    ${product.brand || "N/A"}
                </p>


                <p>
                    <strong>Category :</strong>
                    ${category}
                </p>


                <div class="price">
                    ₹ ${product.price}
                </div>


                <div class="buttons">

                    <button
                        class="details"
                        onclick="viewProduct(${product.id})">

                        View Details

                    </button>


                    <button
                        class="cart"
                        onclick="addToCart(${product.id})">

                        Add Cart 🛒

                    </button>


                    <button
                        class="wishlist"
                        onclick="addWishlist(${product.id})">

                        ❤️

                    </button>

                </div>

            </div>

        `;

    });

}


// =====================================
// VIEW PRODUCT DETAILS
// =====================================

function viewProduct(id) {

    window.location.href =
        `uproduct-details.html?id=${id}`;

}


// =====================================
// ADD TO CART
// =====================================

function addToCart(productId) {

    const token =
        localStorage.getItem("token");


    if (!token) {

        alert("Please login first");

        window.location.href =
            "login.html";

        return;
    }


    fetch(
        `${CART_API}/add?productId=${productId}&quantity=1`,
        {
            method: "POST",

            headers: {
                "Authorization":
                    "Bearer " + token
            }
        }
    )

    .then(res => {

        if (!res.ok) {

            throw new Error(
                "Unable to add cart"
            );

        }

        return res.text();

    })

    .then(() => {

        alert("Product added to Cart 🛒");

        window.location.href =
            "cart.html";

    })

    .catch(err => {

        console.error(err);

        alert("Unable to add cart");

    });

}


// =====================================
// ADD TO WISHLIST
// =====================================

function addWishlist(productId) {

    const token =
        localStorage.getItem("token");


    if (!token) {

        alert("Please login first");

        window.location.href =
            "login.html";

        return;
    }


    fetch(
        `${WISHLIST_API}/add?productId=${productId}`,
        {
            method: "POST",

            headers: {
                "Authorization":
                    "Bearer " + token
            }
        }
    )

    .then(res => {

        if (!res.ok) {

            return res.text()
                .then(msg => {

                    throw new Error(msg);

                });

        }

        return res.text();

    })

    .then(() => {

        alert("Added to Wishlist ❤️");

    })

    .catch(err => {

        console.error(err);

        alert(err.message);

    });

}


// =====================================
// SEARCH PRODUCTS
// =====================================

function searchProducts() {

    currentPage = 0;

    loadFilteredProducts();

}


// =====================================
// MAIN FILTER FUNCTION
//
// Handles:
// Search
// Category
// Search + Category
// Price Sorting
// Pagination
// =====================================

function loadFilteredProducts() {

    const keyword =
        document.getElementById("searchInput")?.value
            .trim() || "";


    const sort =
        document.getElementById("priceRange")?.value
            || "";


    let url;


    // =====================================
    // SEARCH + CATEGORY
    // =====================================

    if (
        selectedCategoryId !== null &&
        keyword !== ""
    ) {

        url =
            `${API}/filter` +
            `?keyword=${encodeURIComponent(keyword)}` +
            `&categoryId=${selectedCategoryId}` +
            `&page=${currentPage}` +
            `&size=${pageSize}`;

    }


    // =====================================
    // CATEGORY ONLY
    // =====================================

    else if (
        selectedCategoryId !== null
    ) {

        url =
            `${API}/category/${selectedCategoryId}` +
            `?page=${currentPage}` +
            `&size=${pageSize}`;

    }


    // =====================================
    // SEARCH ONLY
    // =====================================

    else if (
        keyword !== ""
    ) {

        url =
            `${API}/filter` +
            `?keyword=${encodeURIComponent(keyword)}` +
            `&page=${currentPage}` +
            `&size=${pageSize}`;

    }


    // =====================================
    // ALL PRODUCTS
    // =====================================

    else {

        url =
            `${API}` +
            `?page=${currentPage}` +
            `&size=${pageSize}`;

    }


    // =====================================
    // PRICE SORT
    // =====================================

    if (sort !== "") {

        url += `&sort=${sort}`;

    }


    console.log("FILTER URL :", url);


    fetch(url)

        .then(res => {

            if (!res.ok) {

                throw new Error(
                    `Request failed: ${res.status}`
                );

            }

            return res.json();

        })

        .then(data => {

            console.log(
                "FILTER RESULT :",
                data
            );

            totalPages =
                data.totalPages;

            displayProducts(
                data.content
            );

            updatePagination();

        })

        .catch(err => {

            console.error(
                "FILTER ERROR :",
                err
            );

        });

}


// =====================================
// CATEGORY FILTER
// =====================================

function loadProductsByCategory(categoryId) {

    selectedCategoryId =
        categoryId;

    currentPage = 0;

    loadFilteredProducts();

}


// =====================================
// LOAD ALL PRODUCTS
// =====================================

function loadAllProducts() {

    selectedCategoryId = null;

    currentPage = 0;


    const price =
        document.getElementById("priceRange");

    if (price) {

        price.value = "";

    }


    const search =
        document.getElementById("searchInput");

    if (search) {

        search.value = "";

    }


    loadProducts();

}


// =====================================
// PRICE SORTING
// =====================================

function filterPrice() {

    currentPage = 0;

    loadFilteredProducts();

}


// =====================================
// PREVIOUS PAGE
// =====================================

function previousPage() {

    if (currentPage > 0) {

        currentPage--;

        loadFilteredProducts();

    }

}


// =====================================
// NEXT PAGE
// =====================================

function nextPage() {

    if (
        currentPage <
        totalPages - 1
    ) {

        currentPage++;

        loadFilteredProducts();

    }

}


// =====================================
// UPDATE PAGINATION
// =====================================

function updatePagination() {

    const page =
        document.getElementById("pageInfo");


    if (page) {

        page.innerText =
            `Page ${totalPages === 0 ? 0 : currentPage + 1} of ${totalPages}`;

    }


    const prev =
        document.getElementById("prevBtn");


    const next =
        document.getElementById("nextBtn");


    if (prev) {

        prev.disabled =
            currentPage === 0;

    }


    if (next) {

        next.disabled =
            totalPages === 0 ||
            currentPage >= totalPages - 1;

    }

}