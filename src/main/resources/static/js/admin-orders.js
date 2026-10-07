// =====================================
// ORDER API
// =====================================

const ORDER_API = "/api/orders/admin/all";


// =====================================
// PAGE LOAD
// =====================================

document.addEventListener(
    "DOMContentLoaded",
    () => {
        loadOrders();
    }
);


// =====================================
// LOAD ALL ORDERS
// =====================================

async function loadOrders() {

    const token = localStorage.getItem("token");

    if (!token) {
        alert("Please login first");
        return;
    }

    try {

        const response = await fetch(
            ORDER_API,
            {
                method: "GET",
                headers: {
                    "Authorization": "Bearer " + token
                }
            }
        );

        if (!response.ok) {
            throw new Error("Order loading failed");
        }

        const orders = await response.json();

        console.log(
            "ADMIN ORDERS :",
            orders
        );

        displayOrders(orders);

    } catch (error) {

        console.error(
            "Order Error :",
            error
        );

        const container =
            document.getElementById(
                "ordersContainer"
            );

        if (container) {

            container.innerHTML = `
                <div class="error-message">

                    <h2>
                        ❌ Failed to Load Orders
                    </h2>

                    <p>
                        Please try again.
                    </p>

                </div>
            `;
        }
    }
}


// =====================================
// DISPLAY ORDERS
// =====================================

function displayOrders(orders) {

    const container =
        document.getElementById(
            "ordersContainer"
        );

    if (!container) {

        console.error(
            "ordersContainer not found"
        );

        return;
    }


    // =================================
    // SAFETY CHECK
    // =================================

    if (!Array.isArray(orders)) {
        orders = [];
    }


    // =================================
    // LATEST ORDERS FIRST
    // =================================
    // Example:
    // 10
    // 9
    // 8
    // 7
    // 6

    orders.sort(
        (a, b) =>
            Number(b.id) - Number(a.id)
    );


    // =================================
    // NO ORDERS
    // =================================

    if (orders.length === 0) {

        container.innerHTML = `
            <div class="no-orders">

                <h2>
                    No Orders Found 📦
                </h2>

            </div>
        `;

        return;
    }


    let html = "";


    // =================================
    // LOOP ORDERS
    // =================================

    orders.forEach(order => {

        const orderStatus =
            order.orderStatus ||
            order.status ||
            "PENDING";


        const paymentStatus =
            order.paymentStatus ||
            "PENDING";


        const returnStatus =
            order.returnStatus ||
            "NONE";


        const returnReason =
            order.returnReason ||
            "";


        html += `

            <div class="order-card">


                <!-- ================================= -->
                <!-- ORDER HEADER -->
                <!-- ================================= -->

                <div class="order-header">

                    <h2>
                        📦 Order #${order.id}
                    </h2>

                </div>


                <!-- ================================= -->
                <!-- ORDER INFORMATION -->
                <!-- ================================= -->

                <div class="order-info">

                    <p>

                        Amount :

                        <b>
                            ₹ ${order.amount || 0}
                        </b>

                    </p>


                    <p>

                        Date :

                        ${order.createdDate || "-"}

                    </p>


                    <p>

                        Payment :

                        <b>
                            ${paymentStatus}
                        </b>

                    </p>


                    <p>

                        Items :

                        ${order.totalItems || 0}

                    </p>

                </div>


                <!-- ================================= -->
                <!-- ORDER ITEMS -->
                <!-- ================================= -->

                <div class="items">

                    ${displayItems(order.items)}

                </div>


                <!-- ================================= -->
                <!-- ORDER STATUS -->
                <!-- ================================= -->

                <div class="status-box">

                    <label>
                        Order Status
                    </label>


                    <select
                        onchange="
                            updateStatus(
                                ${order.id},
                                this.value
                            )
                        "
                    >

                        <option
                            value="PENDING"
                            ${
                                orderStatus === "PENDING"
                                    ? "selected"
                                    : ""
                            }
                        >
                            PENDING
                        </option>


                        <option
                            value="CONFIRMED"
                            ${
                                orderStatus === "CONFIRMED"
                                    ? "selected"
                                    : ""
                            }
                        >
                            CONFIRMED
                        </option>


                        <option
                            value="PROCESSING"
                            ${
                                orderStatus === "PROCESSING"
                                    ? "selected"
                                    : ""
                            }
                        >
                            PROCESSING
                        </option>


                        <option
                            value="SHIPPED"
                            ${
                                orderStatus === "SHIPPED"
                                    ? "selected"
                                    : ""
                            }
                        >
                            SHIPPED
                        </option>


                        <option
                            value="OUT_FOR_DELIVERY"
                            ${
                                orderStatus === "OUT_FOR_DELIVERY"
                                    ? "selected"
                                    : ""
                            }
                        >
                            OUT FOR DELIVERY
                        </option>


                        <option
                            value="DELIVERED"
                            ${
                                orderStatus === "DELIVERED"
                                    ? "selected"
                                    : ""
                            }
                        >
                            DELIVERED
                        </option>


                        <option
                            value="CANCELLED"
                            ${
                                orderStatus === "CANCELLED"
                                    ? "selected"
                                    : ""
                            }
                        >
                            CANCELLED
                        </option>

                    </select>

                </div>


                <!-- ================================= -->
                <!-- RETURN MANAGEMENT -->
                <!-- ================================= -->

                ${getReturnManagementHTML(
                    order.id,
                    returnStatus,
                    returnReason
                )}

            </div>

        `;
    });


    container.innerHTML = html;
}


// =====================================
// DISPLAY ORDER ITEMS
// =====================================

function displayItems(items) {

    if (!Array.isArray(items) || items.length === 0) {

        return `
            <p class="no-items">
                No Items Found
            </p>
        `;
    }


    let html = "";


    items.forEach(item => {

        html += `

            <div class="item-card">


                <img
                    src="${
                        item.productImage ||
                        "/images/no-image.png"
                    }"

                    alt="${
                        item.productName ||
                        "Product"
                    }"

                    onerror="
                        this.src='/images/no-image.png'
                    "
                >


                <div>

                    <h3>

                        ${
                            item.productName ||
                            "Product"
                        }

                    </h3>


                    <p>

                        Price :

                        ₹ ${item.price || 0}

                    </p>


                    <p>

                        Qty :

                        ${item.quantity || 1}

                    </p>


                    <p>

                        Sub Total :

                        ₹ ${item.subTotal || 0}

                    </p>

                </div>

            </div>

        `;
    });


    return html;
}


// =====================================
// RETURN MANAGEMENT HTML
// =====================================

function getReturnManagementHTML(
    orderId,
    returnStatus,
    returnReason
) {


    // =================================
    // NO RETURN REQUEST
    // =================================

    if (returnStatus === "NONE") {

        return `

            <div class="admin-return-section">

                <h3>
                    🔄 Return Management
                </h3>


                <div class="return-status none">

                    No Return Request

                </div>

            </div>

        `;
    }


    // =================================
    // RETURN REQUESTED
    // =================================

    if (returnStatus === "REQUESTED") {

        return `

            <div class="admin-return-section">

                <h3>
                    🔄 Return Management
                </h3>


                <div class="return-status requested">

                    ↩ Return Requested

                </div>


                <!-- RETURN REASON -->

                <div class="return-reason-box">

                    <strong>
                        Return Reason
                    </strong>


                    <p>

                        ${
                            returnReason ||
                            "Reason not provided"
                        }

                    </p>

                </div>


                <!-- RETURN ACTIONS -->

                <div class="return-actions">


                    <button
                        class="approve-return-btn"

                        onclick="
                            approveReturn(${orderId})
                        "
                    >

                        ✓ Approve Return

                    </button>


                    <button
                        class="reject-return-btn"

                        onclick="
                            rejectReturn(${orderId})
                        "
                    >

                        ✕ Reject Return

                    </button>


                </div>

            </div>

        `;
    }


    // =================================
    // RETURN APPROVED
    // =================================

    if (returnStatus === "APPROVED") {

        return `

            <div class="admin-return-section">

                <h3>
                    🔄 Return Management
                </h3>


                <div class="return-status approved">

                    ✓ Return Approved

                </div>


                <!-- RETURN REASON -->

                <div class="return-reason-box">

                    <strong>
                        Return Reason
                    </strong>


                    <p>

                        ${
                            returnReason ||
                            "Reason not provided"
                        }

                    </p>

                </div>


                <!-- COMPLETE RETURN -->

                <div class="return-actions">

                    <button
                        class="complete-return-btn"

                        onclick="
                            completeReturn(${orderId})
                        "
                    >

                        ✓ Complete Return

                    </button>

                </div>

            </div>

        `;
    }


    // =================================
    // RETURN REJECTED
    // =================================

    if (returnStatus === "REJECTED") {

        return `

            <div class="admin-return-section">

                <h3>
                    🔄 Return Management
                </h3>


                <div class="return-status rejected">

                    ✕ Return Request Rejected

                </div>


                <!-- RETURN REASON -->

                <div class="return-reason-box">

                    <strong>
                        Return Reason
                    </strong>


                    <p>

                        ${
                            returnReason ||
                            "Reason not provided"
                        }

                    </p>

                </div>

            </div>

        `;
    }


    // =================================
    // PRODUCT RETURNED
    // =================================

    if (returnStatus === "RETURNED") {

        return `

            <div class="admin-return-section">

                <h3>
                    🔄 Return Management
                </h3>


                <div class="return-status returned">

                    ✓ Product Returned

                </div>


                <!-- RETURN REASON -->

                <div class="return-reason-box">

                    <strong>
                        Return Reason
                    </strong>


                    <p>

                        ${
                            returnReason ||
                            "Reason not provided"
                        }

                    </p>

                </div>

            </div>

        `;
    }


    // =================================
    // DEFAULT
    // =================================

    return `

        <div class="admin-return-section">

            <h3>
                🔄 Return Management
            </h3>


            <div class="return-status none">

                No Return Request

            </div>

        </div>

    `;
}


// =====================================
// UPDATE ORDER STATUS
// =====================================

async function updateStatus(
    id,
    status
) {

    const token =
        localStorage.getItem("token");


    if (!token) {

        alert("Please login first");

        return;
    }


    try {

        const response = await fetch(

            `/api/orders/admin/${id}/status?status=${status}`,

            {
                method: "PUT",

                headers: {
                    "Authorization":
                        "Bearer " + token
                }
            }

        );


        if (!response.ok) {

            const message =
                await response.text();

            throw new Error(
                message ||
                "Status update failed"
            );
        }


        const updatedOrder =
            await response.json();


        console.log(
            "Status Updated :",
            updatedOrder
        );


        alert(
            "✅ Order Status Updated Successfully"
        );


        // Reload orders
        loadOrders();


    } catch (error) {

        console.error(
            "Status Update Error :",
            error
        );


        alert(
            "❌ Order Status Update Failed"
        );
    }
}


// =====================================
// APPROVE RETURN
// =====================================

async function approveReturn(
    orderId
) {

    const confirmed =
        confirm(
            "Approve this return request?"
        );


    if (!confirmed) {
        return;
    }


    const token =
        localStorage.getItem("token");


    if (!token) {

        alert("Please login first");

        return;
    }


    try {

        const response =
            await fetch(

                `/api/orders/return/${orderId}/approve`,

                {
                    method: "PUT",

                    headers: {
                        "Authorization":
                            "Bearer " + token
                    }
                }

            );


        if (!response.ok) {

            const message =
                await response.text();

            throw new Error(
                message ||
                "Return approval failed"
            );
        }


        const updatedOrder =
            await response.json();


        console.log(
            "Return Approved :",
            updatedOrder
        );


        alert(
            "✅ Return Approved Successfully"
        );


        loadOrders();


    } catch (error) {

        console.error(
            "Approve Return Error :",
            error
        );


        alert(
            "❌ Failed to Approve Return"
        );
    }
}


// =====================================
// REJECT RETURN
// =====================================

async function rejectReturn(
    orderId
) {

    const confirmed =
        confirm(
            "Reject this return request?"
        );


    if (!confirmed) {
        return;
    }


    const token =
        localStorage.getItem("token");


    if (!token) {

        alert("Please login first");

        return;
    }


    try {

        const response =
            await fetch(

                `/api/orders/return/${orderId}/reject`,

                {
                    method: "PUT",

                    headers: {
                        "Authorization":
                            "Bearer " + token
                    }
                }

            );


        if (!response.ok) {

            const message =
                await response.text();

            throw new Error(
                message ||
                "Return rejection failed"
            );
        }


        const updatedOrder =
            await response.json();


        console.log(
            "Return Rejected :",
            updatedOrder
        );


        alert(
            "❌ Return Request Rejected"
        );


        loadOrders();


    } catch (error) {

        console.error(
            "Reject Return Error :",
            error
        );


        alert(
            "❌ Failed to Reject Return"
        );
    }
}


// =====================================
// COMPLETE RETURN
// =====================================

async function completeReturn(
    orderId
) {

    const confirmed =
        confirm(
            "Confirm that the product has been returned?"
        );


    if (!confirmed) {
        return;
    }


    const token =
        localStorage.getItem("token");


    if (!token) {

        alert("Please login first");

        return;
    }


    try {

        const response =
            await fetch(

                `/api/orders/return/${orderId}/complete`,

                {
                    method: "PUT",

                    headers: {
                        "Authorization":
                            "Bearer " + token
                    }
                }

            );


        if (!response.ok) {

            const message =
                await response.text();

            throw new Error(
                message ||
                "Return completion failed"
            );
        }


        const updatedOrder =
            await response.json();


        console.log(
            "Return Completed :",
            updatedOrder
        );


        alert(
            "✅ Product Return Completed Successfully"
        );


        loadOrders();


    } catch (error) {

        console.error(
            "Complete Return Error :",
            error
        );


        alert(
            "❌ Failed to Complete Return"
        );
    }
}