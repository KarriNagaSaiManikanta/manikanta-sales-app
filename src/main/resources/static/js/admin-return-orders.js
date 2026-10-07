/* =====================================
   ADMIN RETURN ORDERS
===================================== */

const RETURN_ORDERS_API = "/api/orders/admin/all";

let allReturnOrders = [];
let currentFilter = "ALL";


/* =====================================
   PAGE LOAD
===================================== */

document.addEventListener("DOMContentLoaded", () => {

    loadReturnOrders();

});


/* =====================================
   LOAD ORDERS
===================================== */

async function loadReturnOrders() {

    const token = localStorage.getItem("token");

    const container =
        document.getElementById("returnOrdersContainer");

    if (!container) {
        console.error(
            "returnOrdersContainer not found"
        );
        return;
    }

    if (!token) {

        container.innerHTML = `
            <div class="error-box">

                <h2>🔐 Login Required</h2>

                <p>
                    Please login as admin to view
                    return orders.
                </p>

            </div>
        `;

        return;
    }


    try {

        const response = await fetch(
            RETURN_ORDERS_API,
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
                "Failed to load orders"
            );

        }


        const orders =
            await response.json();


        console.log(
            "ALL ADMIN ORDERS:",
            orders
        );


        if (!Array.isArray(orders)) {

            allReturnOrders = [];

        } else {

            /*
             * Only orders having
             * return request/status
             */

            allReturnOrders =
                orders.filter(order => {

                    const returnStatus =
                        (
                            order.returnStatus ||
                            "NONE"
                        )
                        .toString()
                        .toUpperCase();

                    return returnStatus !== "NONE";

                });

        }


        /*
         * Latest order first
         */

        allReturnOrders.sort(
            (a, b) =>
                Number(b.id) -
                Number(a.id)
        );


        updateSummary();

        displayReturnOrders();


    } catch (error) {

        console.error(
            "Return Orders Error:",
            error
        );


        container.innerHTML = `
            <div class="error-box">

                <h2>
                    ❌ Failed to Load Return Orders
                </h2>

                <p>
                    Please refresh the page and try again.
                </p>

            </div>
        `;

    }

}


/* =====================================
   SUMMARY
===================================== */

function updateSummary() {

    let requested = 0;
    let approved = 0;
    let returned = 0;


    allReturnOrders.forEach(order => {

        const status =
            (
                order.returnStatus ||
                "NONE"
            )
            .toString()
            .toUpperCase();


        if (status === "REQUESTED") {

            requested++;

        }

        else if (status === "APPROVED") {

            approved++;

        }

        else if (status === "RETURNED") {

            returned++;

        }

    });


    const pendingElement =
        document.getElementById(
            "pendingReturns"
        );

    const approvedElement =
        document.getElementById(
            "approvedReturns"
        );

    const completedElement =
        document.getElementById(
            "completedReturns"
        );


    if (pendingElement) {

        pendingElement.textContent =
            requested;

    }


    if (approvedElement) {

        approvedElement.textContent =
            approved;

    }


    if (completedElement) {

        completedElement.textContent =
            returned;

    }

}


/* =====================================
   DISPLAY RETURN ORDERS
===================================== */

function displayReturnOrders() {

    const container =
        document.getElementById(
            "returnOrdersContainer"
        );


    if (!container) return;


    let orders = [...allReturnOrders];


    /*
     * Apply filter
     */

    if (currentFilter !== "ALL") {

        orders =
            orders.filter(order => {

                const status =
                    (
                        order.returnStatus ||
                        "NONE"
                    )
                    .toString()
                    .toUpperCase();

                return status === currentFilter;

            });

    }


    /*
     * No orders
     */

    if (orders.length === 0) {

        container.innerHTML = `

            <div class="empty-box">

                <h2>
                    📦 No Return Orders
                </h2>

                <p>
                    No return orders found
                    for this filter.
                </p>

            </div>

        `;

        return;

    }


    let html = "";


    orders.forEach(order => {

        html += createReturnCard(order);

    });


    container.innerHTML = html;

}


/* =====================================
   CREATE RETURN CARD
===================================== */

function createReturnCard(order) {

    const orderId =
        order.id || "-";


    const returnStatus =
        (
            order.returnStatus ||
            "NONE"
        )
        .toString()
        .toUpperCase();


    const returnReason =
        order.returnReason ||
        "Reason not provided";


    const amount =
        order.amount ??
        order.totalAmount ??
        order.total_amount ??
        order.total ??
        0;


    const paymentStatus =
        (
            order.paymentStatus ||
            "PENDING"
        )
        .toString()
        .toUpperCase();


    const orderStatus =
        (
            order.orderStatus ||
            order.status ||
            "PENDING"
        )
        .toString()
        .toUpperCase();


    const customerName =
        order.customerName ||
        order.customer_name ||
        order.userName ||
        order.user?.name ||
        order.user?.firstName ||
        order.userEmail ||
        order.user_email ||
        "Customer";


    const customerEmail =
        order.userEmail ||
        order.user_email ||
        order.user?.email ||
        "-";


    const createdDate =
        order.createdDate ||
        order.created_date ||
        "-";


    const statusText =
        formatReturnStatus(
            returnStatus
        );


    let actionsHTML = "";


    /* =================================
       REQUESTED
    ================================= */

    if (returnStatus === "REQUESTED") {

        actionsHTML = `

            <div class="return-actions">

                <button
                    class="approve-btn"
                    onclick="approveReturn(${orderId})"
                >
                    ✓ Approve Return
                </button>


                <button
                    class="reject-btn"
                    onclick="rejectReturn(${orderId})"
                >
                    ✕ Reject Return
                </button>

            </div>

        `;

    }


    /* =================================
       APPROVED
    ================================= */

    else if (returnStatus === "APPROVED") {

        actionsHTML = `

            <div class="return-actions">

                <button
                    class="complete-btn"
                    onclick="completeReturn(${orderId})"
                >
                    ✓ Complete Return
                </button>

            </div>

        `;

    }


    /* =================================
       RETURNED
    ================================= */

    else if (returnStatus === "RETURNED") {

        actionsHTML = `

            <div class="return-actions">

                <button
                    class="complete-btn"
                    disabled
                >
                    ✓ Return Completed
                </button>

            </div>

        `;

    }


    /* =================================
       REJECTED
    ================================= */

    else if (returnStatus === "REJECTED") {

        actionsHTML = `

            <div class="return-actions">

                <button
                    class="reject-btn"
                    disabled
                >
                    ✕ Return Rejected
                </button>

            </div>

        `;

    }


    return `

        <div
            class="return-card"
            data-status="${returnStatus}"
        >


            <!-- CARD HEADER -->

            <div class="return-card-header">

                <h2>
                    📦 Order #${orderId}
                </h2>


                <span
                    class="
                        return-status
                        ${getReturnStatusClass(returnStatus)}
                    "
                >
                    ${statusText}
                </span>

            </div>


            <!-- CUSTOMER -->

            <div class="customer-info">

                <h3>
                    👤 Customer Details
                </h3>


                <div class="info-row">

                    <span>
                        Customer
                    </span>

                    <span>
                        ${escapeHTML(customerName)}
                    </span>

                </div>


                <div class="info-row">

                    <span>
                        Email
                    </span>

                    <span>
                        ${escapeHTML(customerEmail)}
                    </span>

                </div>


                <div class="info-row">

                    <span>
                        Order Date
                    </span>

                    <span>
                        ${escapeHTML(createdDate)}
                    </span>

                </div>

            </div>


            <!-- ORDER DETAILS -->

            <div class="customer-info">

                <h3>
                    📋 Order Details
                </h3>


                <div class="info-row">

                    <span>
                        Order Status
                    </span>

                    <span>
                        ${formatOrderStatus(orderStatus)}
                    </span>

                </div>


                <div class="info-row">

                    <span>
                        Order Amount
                    </span>

                    <span>
                        ₹ ${formatMoney(amount)}
                    </span>

                </div>


                <div class="info-row">

                    <span>
                        Payment
                    </span>

                    <span>
                        ${formatPaymentStatus(paymentStatus)}
                    </span>

                </div>

            </div>


            <!-- RETURN REASON -->

            <div class="return-reason">

                <strong>
                    🔄 Return Reason
                </strong>

                <p>
                    ${escapeHTML(returnReason)}
                </p>

            </div>


            <!-- ACTIONS -->

            ${actionsHTML}


        </div>

    `;

}


/* =====================================
   FILTER
===================================== */

function filterReturns(status) {

    currentFilter = status;


    /*
     * Active button
     */

    const buttons =
        document.querySelectorAll(
            ".filter-btn"
        );


    buttons.forEach(button => {

        button.classList.remove(
            "active"
        );


        if (
            button.dataset.status ===
            status
        ) {

            button.classList.add(
                "active"
            );

        }

    });


    displayReturnOrders();

}


/* =====================================
   APPROVE RETURN
===================================== */

async function approveReturn(orderId) {

    const confirmed =
        confirm(
            "Approve this return request?"
        );


    if (!confirmed) return;


    await updateReturnStatus(
        `/api/orders/return/${orderId}/approve`,
        "Return Approved Successfully"
    );

}


/* =====================================
   REJECT RETURN
===================================== */

async function rejectReturn(orderId) {

    const confirmed =
        confirm(
            "Reject this return request?"
        );


    if (!confirmed) return;


    await updateReturnStatus(
        `/api/orders/return/${orderId}/reject`,
        "Return Request Rejected"
    );

}


/* =====================================
   COMPLETE RETURN
===================================== */

async function completeReturn(orderId) {

    const confirmed =
        confirm(
            "Confirm that the product has been returned?"
        );


    if (!confirmed) return;


    await updateReturnStatus(
        `/api/orders/return/${orderId}/complete`,
        "Product Return Completed Successfully"
    );

}


/* =====================================
   UPDATE RETURN STATUS
===================================== */

async function updateReturnStatus(
    url,
    successMessage
) {

    const token =
        localStorage.getItem("token");


    if (!token) {

        alert(
            "Please login first"
        );

        return;

    }


    try {

        const response =
            await fetch(
                url,
                {
                    method: "PUT",

                    headers: {
                        "Authorization":
                            "Bearer " + token,

                        "Accept":
                            "application/json"
                    }
                }
            );


        if (!response.ok) {

            const message =
                await response.text();

            throw new Error(
                message ||
                "Return update failed"
            );

        }


        const updatedOrder =
            await response.json();


        console.log(
            "RETURN UPDATED:",
            updatedOrder
        );


        alert(
            "✅ " + successMessage
        );


        /*
         * Reload latest data
         */

        await loadReturnOrders();


    } catch (error) {

        console.error(
            "Return Update Error:",
            error
        );


        alert(
            "❌ Failed to update return"
        );

    }

}


/* =====================================
   RETURN STATUS TEXT
===================================== */

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
            char => char.toUpperCase()
        );

}


/* =====================================
   RETURN STATUS CLASS
===================================== */

function getReturnStatusClass(status) {

    switch (
        (status || "")
            .toString()
            .toUpperCase()
    ) {

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


/* =====================================
   ORDER STATUS
===================================== */

function formatOrderStatus(status) {

    if (!status) {
        return "Pending";
    }


    return status
        .toString()
        .replace(/_/g, " ")
        .toLowerCase()
        .replace(
            /\b\w/g,
            char => char.toUpperCase()
        );

}


/* =====================================
   PAYMENT STATUS
===================================== */

function formatPaymentStatus(status) {

    if (!status) {
        return "Pending";
    }


    return status
        .toString()
        .replace(/_/g, " ")
        .toLowerCase()
        .replace(
            /\b\w/g,
            char => char.toUpperCase()
        );

}


/* =====================================
   MONEY
===================================== */

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


/* =====================================
   ESCAPE HTML
===================================== */

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