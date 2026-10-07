// ============================================
// API URL
// ============================================

// API URLs are already available from api.js


// ============================================
// DOM LOAD
// ============================================

document.addEventListener("DOMContentLoaded", () => {

    loadAdminProfile();

    loadProductCount();

    loadCategoryCount();

    loadUserCount();

    loadOrderCount();

    loadRevenue();

    loadRecentOrders();

    loadReturnOrders();

});


// ============================================
// LOAD ADMIN PROFILE
// ============================================

function loadAdminProfile() {

    const token =
        localStorage.getItem("token");


    if (!token) {

        console.log("Admin token not found");

        return;

    }


    fetch(
        "/api/users/profile",
        {
            method: "GET",

            headers: {

                "Authorization":
                    "Bearer " + token,

                "Accept":
                    "application/json"

            }

        }
    )

    .then(response => {

        if (!response.ok) {

            throw new Error(
                "Profile loading failed"
            );

        }

        return response.json();

    })

    .then(data => {

        console.log(
            "Admin Profile:",
            data
        );


        // ==========================
        // ADMIN NAME
        // ==========================

        const name =
            document.getElementById(
                "adminName"
            );


        if (name) {

            name.innerText =
                data.fullName ||
                data.name ||
                "Admin";

        }


        // ==========================
        // ADMIN EMAIL
        // ==========================

        const email =
            document.getElementById(
                "adminEmail"
            );


        if (email) {

            email.innerText =
                data.email ||
                "-";

        }


        // ==========================
        // ADMIN PROFILE IMAGE
        // ==========================

        const image =
            document.getElementById(
                "adminProfileImage"
            );


        if (image) {

            if (
                data.profileImage &&
                data.profileImage.trim() !== ""
            ) {

                image.src =
                    window.location.origin +
                    data.profileImage +
                    "?t=" +
                    new Date().getTime();

            }

            else {

                image.src =
                    "../images/admin.png";

            }


            image.onerror = function () {

                this.src =
                    "../images/admin.png";

            };

        }

    })

    .catch(error => {

        console.log(
            "Profile Error:",
            error
        );

    });

}


// ============================================
// LOAD TOTAL PRODUCTS
// ============================================

async function loadProductCount() {

    const token =
        localStorage.getItem("token");


    try {

        const response =
            await fetch(
                PRODUCT_API + "/count",
                {
                    method: "GET",

                    headers: {

                        "Authorization":
                            "Bearer " + token,

                        "Accept":
                            "application/json"

                    }

                }
            );


        if (!response.ok) {

            throw new Error(
                "Failed to load product count"
            );

        }


        const total =
            await response.json();


        const element =
            document.getElementById(
                "totalProducts"
            );


        if (element) {

            element.innerText =
                total;

        }

    }

    catch (error) {

        console.log(
            "Product Count Error:",
            error
        );

    }

}


// ============================================
// LOAD TOTAL CATEGORIES
// ============================================

async function loadCategoryCount() {

    const token =
        localStorage.getItem("token");


    try {

        const response =
            await fetch(
                CATEGORY_API + "/count",
                {
                    method: "GET",

                    headers: {

                        "Authorization":
                            "Bearer " + token,

                        "Accept":
                            "application/json"

                    }

                }
            );


        if (!response.ok) {

            throw new Error(
                "Failed to load category count"
            );

        }


        const total =
            await response.json();


        const element =
            document.getElementById(
                "totalCategories"
            );


        if (element) {

            element.innerText =
                total;

        }

    }

    catch (error) {

        console.log(
            "Category Count Error:",
            error
        );

    }

}


// ============================================
// LOAD TOTAL USERS
// ============================================

async function loadUserCount() {

    const token =
        localStorage.getItem("token");


    try {

        const response =
            await fetch(
                API_BASE_URL + "/users/count",
                {
                    method: "GET",

                    headers: {

                        "Authorization":
                            "Bearer " + token,

                        "Accept":
                            "application/json"

                    }

                }
            );


        if (!response.ok) {

            throw new Error(
                "Failed to load users"
            );

        }


        const total =
            await response.json();


        const element =
            document.getElementById(
                "totalUsers"
            );


        if (element) {

            element.innerText =
                total;

        }

    }

    catch (error) {

        console.log(
            "User Count Error:",
            error
        );

    }

}


// ============================================
// LOAD TOTAL ORDERS
// ============================================

async function loadOrderCount() {

    const token =
        localStorage.getItem("token");


    try {

        const response =
            await fetch(
                ORDER_API + "/count",
                {
                    method: "GET",

                    headers: {

                        "Authorization":
                            "Bearer " + token,

                        "Accept":
                            "application/json"

                    }

                }
            );


        if (!response.ok) {

            throw new Error(
                "Order count loading failed"
            );

        }


        const total =
            await response.json();


        const element =
            document.getElementById(
                "totalOrders"
            );


        if (element) {

            element.innerText =
                total;

        }

    }

    catch (error) {

        console.log(
            "Order Count Error:",
            error
        );

    }

}


// ============================================
// LOAD TOTAL REVENUE
// ============================================

async function loadRevenue() {

    const token =
        localStorage.getItem("token");


    try {

        const response =
            await fetch(
                ORDER_API + "/revenue",
                {
                    method: "GET",

                    headers: {

                        "Authorization":
                            "Bearer " + token,

                        "Accept":
                            "application/json"

                    }

                }
            );


        if (!response.ok) {

            throw new Error(
                "Revenue loading failed"
            );

        }


        const revenue =
            await response.json();


        console.log(
            "Revenue Response:",
            revenue
        );


        const element =
            document.getElementById(
                "totalRevenue"
            );


        if (element) {

            element.innerText =
                "₹ " +
                Number(
                    revenue.totalRevenue || 0
                ).toLocaleString("en-IN");

        }

    }

    catch (error) {

        console.log(
            "Revenue Error:",
            error
        );

    }

}


// ============================================
// OPEN REVENUE
// ============================================

function openRevenue() {

    window.location.href =
        "revenue.html";

}


// ============================================
// OPEN PRODUCTS
// ============================================

function openProducts() {

    window.location.href =
        "products.html";

}


// ============================================
// OPEN USERS
// ============================================

function openUsers() {

    window.location.href =
        "users.html";

}


// ============================================
// OPEN CATEGORIES
// ============================================

function openCategories() {

    window.location.href =
        "categories.html";

}


// ============================================
// OPEN ORDERS
// ============================================

function openOrders() {

    window.location.href =
        "orders.html";

}


// ============================================
// OPEN RETURN ORDERS
// ============================================

function openReturnOrders() {

    window.location.href =
        "admin-return-orders.html";

}


// ============================================
// CUSTOMER / USER ID
// ============================================

function getCustomer(order) {

    /*
     * Priority:
     *
     * 1. Customer name
     * 2. User name
     * 3. Customer email
     * 4. User ID
     *
     * If backend only sends userId,
     * dashboard will show:
     *
     * User #5
     */


    const customerName =

        order.customerName ||

        order.customer_name ||

        order.userName ||

        order.user_name ||

        order.fullName ||

        order.full_name ||

        order.user?.fullName ||

        order.user?.full_name ||

        order.user?.name ||

        order.user?.firstName ||

        order.customer?.fullName ||

        order.customer?.full_name ||

        order.customer?.name ||

        order.customer?.firstName;


    if (customerName) {

        return customerName;

    }


    // ==========================
    // USER ID
    // ==========================

    const userId =

        order.userId ||

        order.user_id ||

        order.user?.id ||

        order.customerId ||

        order.customer_id ||

        order.customer?.id;


    if (
        userId !== null &&
        userId !== undefined &&
        userId !== ""
    ) {

        return "User #" + userId;

    }


    // ==========================
    // EMAIL
    // ==========================

    const email =

        order.userEmail ||

        order.user_email ||

        order.user?.email ||

        order.customer?.email;


    if (email) {

        return email;

    }


    return "-";

}


// ============================================
// LOAD RECENT ORDERS
// ============================================

async function loadRecentOrders() {

    const token =
        localStorage.getItem("token");


    const container =
        document.getElementById(
            "recentOrders"
        );


    if (!container) {

        console.error(
            "recentOrders element not found"
        );

        return;

    }


    if (!token) {

        container.innerHTML = `

            <tr>

                <td colspan="4">

                    Please login first

                </td>

            </tr>

        `;

        return;

    }


    try {

        const response =
            await fetch(
                "/api/orders/recent",
                {
                    method: "GET",

                    headers: {

                        "Authorization":
                            "Bearer " + token,

                        "Accept":
                            "application/json"

                    }

                }
            );


        if (!response.ok) {

            throw new Error(
                "Failed to load recent orders"
            );

        }


        let orders =
            await response.json();


        console.log(
            "RECENT ORDERS:",
            orders
        );


        if (!Array.isArray(orders)) {

            orders = [];

        }


        // ==========================
        // LATEST ORDERS FIRST
        // ==========================

        orders.sort(
            (a, b) =>
                Number(b.id) -
                Number(a.id)
        );


        // ==========================
        // ONLY 5 ORDERS
        // ==========================

        orders =
            orders.slice(0, 5);


        if (orders.length === 0) {

            container.innerHTML = `

                <tr>

                    <td colspan="4">

                        No Orders Available

                    </td>

                </tr>

            `;

            return;

        }


        let html = "";


        orders.forEach(order => {


            // ==========================
            // ORDER STATUS
            // ==========================

            const orderStatus =

                order.orderStatus ||

                order.status ||

                "PENDING";


            // ==========================
            // ORDER AMOUNT
            // ==========================

            const amount =

                order.amount ??

                order.totalAmount ??

                order.total_amount ??

                order.total ??

                0;


            // ==========================
            // CUSTOMER / USER
            // ==========================

            const customer =
                getCustomer(order);


            console.log(
                "ORDER CUSTOMER:",
                order.id,
                customer,
                order
            );


            html += `

                <tr>

                    <td>

                        #${order.id}

                    </td>


                    <td>

                        ${escapeHTML(customer)}

                    </td>


                    <td>

                        <span
                            class="order-status ${getStatusClass(orderStatus)}"
                        >

                            ${formatStatus(orderStatus)}

                        </span>

                    </td>


                    <td>

                        ₹ ${formatMoney(amount)}

                    </td>

                </tr>

            `;

        });


        container.innerHTML =
            html;


    }

    catch (error) {

        console.error(
            "Recent Orders Error:",
            error
        );


        container.innerHTML = `

            <tr>

                <td colspan="4">

                    ❌ Failed to load orders

                </td>

            </tr>

        `;

    }

}


// ============================================
// LOAD RETURN ORDERS
// ============================================

async function loadReturnOrders() {

    const token =
        localStorage.getItem("token");


    const container =
        document.getElementById(
            "returnOrders"
        );


    if (!container) {

        console.error(
            "returnOrders element not found"
        );

        return;

    }


    if (!token) {

        container.innerHTML = `

            <tr>

                <td colspan="4">

                    Please login first

                </td>

            </tr>

        `;

        return;

    }


    try {

        const response =
            await fetch(
                "/api/orders/admin/all",
                {
                    method: "GET",

                    headers: {

                        "Authorization":
                            "Bearer " + token,

                        "Accept":
                            "application/json"

                    }

                }
            );


        if (!response.ok) {

            throw new Error(
                "Failed to load return orders"
            );

        }


        let orders =
            await response.json();


        console.log(
            "ALL ORDERS FOR RETURNS:",
            orders
        );


        if (!Array.isArray(orders)) {

            orders = [];

        }


        // ==========================
        // ONLY RETURN ORDERS
        // ==========================

        let returnOrders =
            orders.filter(order => {

                const returnStatus =

                    (
                        order.returnStatus ||

                        "NONE"
                    )
                    .toString()
                    .toUpperCase()
                    .trim();


                return (
                    returnStatus !==
                    "NONE"
                );

            });


        // ==========================
        // LATEST RETURN FIRST
        // ==========================

        returnOrders.sort(
            (a, b) =>
                Number(b.id) -
                Number(a.id)
        );


        // ==========================
        // ONLY 5
        // ==========================

        returnOrders =
            returnOrders.slice(0, 5);


        if (
            returnOrders.length === 0
        ) {

            container.innerHTML = `

                <tr>

                    <td colspan="4">

                        No Return Orders

                    </td>

                </tr>

            `;

            return;

        }


        let html = "";


        returnOrders.forEach(order => {


            // ==========================
            // CUSTOMER / USER
            // ==========================

            const customer =
                getCustomer(order);


            // ==========================
            // RETURN STATUS
            // ==========================

            const returnStatus =

                (
                    order.returnStatus ||

                    "NONE"
                )
                .toString()
                .toUpperCase()
                .trim();


            console.log(
                "RETURN CUSTOMER:",
                order.id,
                customer,
                order
            );


            html += `

                <tr>

                    <td>

                        #${order.id}

                    </td>


                    <td>

                        ${escapeHTML(customer)}

                    </td>


                    <td>

                        <span
                            class="return-status ${getReturnStatusClass(returnStatus)}"
                        >

                            ${formatReturnStatus(returnStatus)}

                        </span>

                    </td>


                    <td>

                       
                    </td>

                </tr>

            `;

        });


        container.innerHTML =
            html;


    }

    catch (error) {

        console.error(
            "Return Orders Error:",
            error
        );


        container.innerHTML = `

            <tr>

                <td colspan="4">

                    ❌ Failed to load return orders

                </td>

            </tr>

        `;

    }

}


// ============================================
// FORMAT ORDER STATUS
// ============================================

function formatStatus(status) {

    if (!status) {

        return "Pending";

    }


    return status
        .toString()
        .replace(/_/g, " ")
        .toLowerCase()
        .replace(
            /\b\w/g,
            char =>
                char.toUpperCase()
        );

}


// ============================================
// ORDER STATUS CSS CLASS
// ============================================

function getStatusClass(status) {

    const value =

        (status || "")
            .toString()
            .toUpperCase();


    switch (value) {

        case "DELIVERED":

            return "delivered";


        case "SHIPPED":

            return "shipped";


        case "OUT_FOR_DELIVERY":

            return "out-for-delivery";


        case "PROCESSING":

            return "processing";


        case "CONFIRMED":

            return "confirmed";


        case "CANCELLED":

            return "cancelled";


        case "PENDING":

        default:

            return "pending";

    }

}


// ============================================
// FORMAT RETURN STATUS
// ============================================

function formatReturnStatus(status) {

    if (!status) {

        return "Unknown";

    }


    return status
        .toString()
        .replace(/_/g, " ")
        .toLowerCase()
        .replace(
            /\b\w/g,
            char =>
                char.toUpperCase()
        );

}


// ============================================
// RETURN STATUS CSS CLASS
// ============================================

function getReturnStatusClass(status) {

    const value =

        (status || "")
            .toString()
            .toUpperCase();


    switch (value) {

        case "REQUESTED":

            return "requested";


        case "APPROVED":

            return "approved";


        case "REJECTED":

            return "rejected";


        case "RETURNED":

            return "returned";


        default:

            return "requested";

    }

}


// ============================================
// FORMAT MONEY
// ============================================

function formatMoney(amount) {

    const value =
        Number(amount) || 0;


    return value.toLocaleString(
        "en-IN",
        {
            minimumFractionDigits: 0,
            maximumFractionDigits: 2
        }
    );

}


// ============================================
// ESCAPE HTML
// ============================================

function escapeHTML(value) {

    if (
        value === null ||
        value === undefined
    ) {

        return "";

    }


    return String(value)

        .replace(
            /&/g,
            "&amp;"
        )

        .replace(
            /</g,
            "&lt;"
        )

        .replace(
            />/g,
            "&gt;"
        )

        .replace(
            /"/g,
            "&quot;"
        )

        .replace(
            /'/g,
            "&#039;"
        );

}