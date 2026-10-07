const API = "/api/cart";

// =================================
// PAGE LOAD
// =================================
document.addEventListener("DOMContentLoaded", () => {

    loadCart();

});

// =================================
// LOAD CART
// =================================
function loadCart() {

    const token = localStorage.getItem("token");

    if (!token) {
        window.location.href = "/login.html";
        return;
    }

    fetch(API, {
        method: "GET",
        headers: {
            "Authorization": "Bearer " + token
        }
    })

    .then(response => {

        if (!response.ok) {
            throw new Error("Cart loading failed");
        }

        return response.json();

    })

    .then(cart => {

        console.log("CART :", cart);

        displayCart(cart);

    })

    .catch(error => {

        console.error(error);

    });

}

// =================================
// DISPLAY CART
// =================================
function displayCart(cart) {

    const container =
        document.getElementById("cartItems");

    if (!container) {
        return;
    }

    container.innerHTML = "";

    if (cart.length === 0) {

        container.innerHTML = `

        <div class="empty-cart">

            <img src="/images/empty-cart.png">

            <h2>Your Cart is Empty 🛒</h2>

        </div>

        `;

        updateTotals(0, 0);

        return;
    }

    cart.forEach(item => {

        let image =
            item.productImage ||
            "/images/no-image.png";

        container.innerHTML += `

        <div class="cart-card">

            <div class="cart-select">

                <input
                    type="checkbox"
                    class="cart-checkbox"
                    value="${item.cartId}"
                    checked
                    onchange="calculateSelectedTotal()">

            </div>

            <img
                class="cart-image"
                src="${image}"
                onerror="this.src='/images/no-image.png'">

            <div class="cart-info">

                <h3>${item.productName}</h3>

                <p>
                    Brand :
                    ${item.brand || "N/A"}
                </p>

                <p>
                    Category :
                    ${item.category || "N/A"}
                </p>

                <p>
                    Price :
                    ₹${item.price}
                </p>

            </div>

            <div class="cart-actions">

                <button
                    onclick="decrease(${item.cartId})">
                    -
                </button>

                <span class="qty">
                    ${item.quantity}
                </span>

                <button
                    onclick="increase(${item.cartId})">
                    +
                </button>

                <br><br>

				<button
				    class="remove-btn"
				    onclick="removeItem(${item.cartId})"
				    style="
				        background:#ffffff;
				        color:#d32f2f;
				        border:1px solid #d32f2f;
				        padding:10px 22px;
				        border-radius:6px;
				        font-size:14px;
				        font-weight:600;
				        cursor:pointer;
				    "
				>
				    Remove
				</button>

            </div>

            <div
                class="item-total"
                data-price="${item.totalPrice}">

                ₹${item.totalPrice}

            </div>

        </div>

        `;

    });

    calculateSelectedTotal();

}
// =================================
// UPDATE TOTALS
// =================================

function updateTotals(items, price) {

    const totalItems =
        document.getElementById("totalItems");

    const totalPrice =
        document.getElementById("totalPrice");

    const grandTotal =
        document.getElementById("grandTotal");

    if (totalItems) {

        totalItems.innerText = items;

    }

    if (totalPrice) {

        totalPrice.innerText =
            "₹" + price.toFixed(2);

    }

    if (grandTotal) {

        grandTotal.innerText =
            "₹" + price.toFixed(2);

    }

}



// =================================
// CALCULATE SELECTED TOTAL
// =================================

function calculateSelectedTotal() {

    let totalItems = 0;
    let totalPrice = 0;

    const cards =
        document.querySelectorAll(".cart-card");

    cards.forEach(card => {

        const checkbox =
            card.querySelector(".cart-checkbox");

        if (checkbox.checked) {

            const qty = parseInt(

                card.querySelector(".qty").innerText

            );

            const price = parseFloat(

                card.querySelector(".item-total")
                    .dataset.price

            );

            totalItems += qty;

            totalPrice += price;

        }

    });

    updateTotals(
        totalItems,
        totalPrice
    );

}



// =================================
// SELECT ALL
// =================================

function selectAllProducts(source) {

    document.querySelectorAll(".cart-checkbox")

    .forEach(box => {

        box.checked = source.checked;

    });

    calculateSelectedTotal();

}



// =================================
// GET SELECTED CART IDS
// =================================

function getSelectedCartIds() {

    let ids = [];

    document.querySelectorAll(".cart-checkbox")

    .forEach(box => {

        if (box.checked) {

            ids.push(

                Number(box.value)

            );

        }

    });

    return ids;

}
// =================================
// INCREASE QUANTITY
// =================================
function increase(cartId, qty, stock){

    if(qty >= stock){
        alert("Only " + stock + " items available");
        return;
    }

    const token = localStorage.getItem("token");

    fetch(API + "/increase/" + cartId,{
        method:"PUT",
        headers:{
            "Authorization":"Bearer " + token
        }
    })
    .then(response=>{
        if(!response.ok){
            throw new Error();
        }

        loadCart();
    })
    .catch(()=>{
        alert("Only limited stock is available");
    });

}

// =================================
// DECREASE QUANTITY
// =================================

function decrease(cartId){

    const token = localStorage.getItem("token");

    fetch(API + "/decrease/" + cartId,{

        method:"PUT",

        headers:{
            "Authorization":"Bearer " + token
        }

    })

    .then(response=>{

        if(!response.ok){
            throw new Error("Unable to decrease quantity");
        }

        return response.text();

    })

    .then(()=>{

        loadCart();

    })

    .catch(error=>{

        console.error(error);

        alert("Unable to decrease quantity");

    });

}



// =================================
// REMOVE CART ITEM
// =================================

function removeItem(cartId){

    if(!confirm("Remove this product from cart?")){
        return;
    }

    const token = localStorage.getItem("token");

    fetch(API + "/remove/" + cartId,{

        method:"DELETE",

        headers:{
            "Authorization":"Bearer " + token
        }

    })

    .then(response=>{

        if(!response.ok){
            throw new Error("Unable to remove product");
        }

        return response.text();

    })

    .then(()=>{

        loadCart();

    })

    .catch(error=>{

        console.error(error);

        alert("Unable to remove product");

    });

}



// =================================
// CLEAR CART
// =================================

function clearCart(){

    if(!confirm("Clear your complete cart?")){
        return;
    }

    const token = localStorage.getItem("token");

    fetch(API + "/clear",{

        method:"DELETE",

        headers:{
            "Authorization":"Bearer " + token
        }

    })

    .then(response=>{

        if(!response.ok){
            throw new Error("Unable to clear cart");
        }

        return response.text();

    })

    .then(()=>{

        loadCart();

    })

    .catch(error=>{

        console.error(error);

        alert("Unable to clear cart");

    });

}
// =================================
// CHECKOUT
// =================================

function checkout() {

    const selectedProducts = [];

    document.querySelectorAll(".cart-checkbox:checked").forEach(item => {
        selectedProducts.push(item.value);
    });

    if (selectedProducts.length === 0) {
        alert("Please select at least one product");
        return;
    }

    localStorage.setItem(
        "selectedCartItems",
        JSON.stringify(selectedProducts)
    );

    window.location.href = "checkout.html";
}