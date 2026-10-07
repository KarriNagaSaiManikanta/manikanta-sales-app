const API_URL = "http://localhost:8080/api/products";

let currentPage = 0;
let pageSize = 10;
let searchKeyword = "";

const token = localStorage.getItem("token");

document.addEventListener("DOMContentLoaded", () => {
    loadProducts();
});

// ======================================
// LOAD PRODUCTS
// ======================================

async function loadProducts() {

    try {

        let url = `${API_URL}?page=${currentPage}&size=${pageSize}`;

        if (searchKeyword !== "") {
            url = `${API_URL}/search?keyword=${encodeURIComponent(searchKeyword)}`;
        }

        const response = await fetch(url, {
            headers: {
                "Authorization": "Bearer " + token
            }
        });

        if (!response.ok) {
            throw new Error("Failed to load products");
        }

        const data = await response.json();

        const products = data.content || data;

        displayProducts(products);

        updatePageNumber();

    } catch (error) {

        console.log(error);

        alert("Unable to load products");

    }

}

// ======================================
// DISPLAY PRODUCTS
// ======================================

function displayProducts(products) {

    const table = document.getElementById("productTable");

    if (!products || products.length === 0) {

        table.innerHTML = `
            <tr>
                <td colspan="8" style="padding:30px;text-align:center;">
                    No Products Found
                </td>
            </tr>
        `;

        return;
    }

    let html = "";

    products.forEach(product => {

        let image = "../images/no-image.png";

        if (product.image && product.image.trim() !== "") {
            image = "http://localhost:8080" + product.image;
        }

        html += `

        <tr>

            <td>
                <img
                    src="${image}"
                    class="product-image"
                    onerror="this.src='../images/no-image.png'">
            </td>

            <td class="product-name">
                ${product.name}
            </td>

            <td>
                ${product.category ? product.category.name : "-"}
            </td>

            <td class="price">
                ₹${product.price}
            </td>

            <td class="stock">
                ${product.quantity}
            </td>

            <td>

                <span class="${product.active ? 'status-active' : 'status-inactive'}">

                    ${product.active ? "Active" : "Inactive"}

                </span>

            </td>

            <td>

                <button
                    class="edit-btn"
                    onclick="editProduct(${product.id})">

                    Edit

                </button>

                <button
                    class="delete-btn"
                    onclick="deleteProduct(${product.id})">

                    Delete

                </button>

            </td>

        </tr>

        `;

    });

    table.innerHTML = html;

}

// ======================================
// SEARCH
// ======================================

function searchProduct() {

    searchKeyword = document.getElementById("searchBox").value.trim();

    currentPage = 0;

    loadProducts();

}

// ======================================
// PAGINATION
// ======================================

function nextPage() {

    currentPage++;

    loadProducts();

}

function previousPage() {

    if (currentPage > 0) {

        currentPage--;

        loadProducts();

    }

}

function updatePageNumber() {

    document.getElementById("pageNumber").innerText = currentPage + 1;

}

// ======================================
// EDIT PRODUCT
// ======================================

function editProduct(id) {

    window.location.href = "add-product.html?id=" + id;

}

// ======================================
// DELETE PRODUCT
// ======================================

async function deleteProduct(id) {

    if (!confirm("Are you sure you want to delete this product?")) {
        return;
    }

    try {

        const response = await fetch(`${API_URL}/${id}`, {

            method: "DELETE",

            headers: {
                "Authorization": "Bearer " + token
            }

        });

        if (response.ok) {

            alert("Product deleted successfully");

            loadProducts();

        } else {

            alert("Delete failed");

        }

    } catch (error) {

        console.log(error);

        alert("Server Error");

    }

}

