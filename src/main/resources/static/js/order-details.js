// =====================================
// ORDER DETAILS
// =====================================

const token = localStorage.getItem("token");
let orderId;


// =====================================
// PAGE LOAD
// =====================================

document.addEventListener("DOMContentLoaded", () => {

    if (!token) {
        window.location.href = "login.html";
        return;
    }

    const params =
        new URLSearchParams(window.location.search);

    orderId =
        params.get("orderId") ||
        params.get("id");

    if (!orderId) {
        showError("Order ID Missing");
        return;
    }

    loadOrder();
});


// =====================================
// LOAD ORDER
// =====================================

function loadOrder() {

    fetch("/api/orders/" + orderId, {
        method: "GET",
        headers: {
            "Authorization": "Bearer " + token
        }
    })

    .then(res => {

        if (res.status === 401) {

            localStorage.clear();

            window.location.href =
                "login.html";

            return;
        }

        if (!res.ok) {

            throw new Error(
                "Unable to load order"
            );
        }

        return res.json();
    })

    .then(order => {

        if (!order) {
            return;
        }

        console.log("=================================");
        console.log("ORDER RESPONSE :", order);
        console.log("ORDER STATUS :", order.orderStatus);
        console.log("STATUS :", order.status);
        console.log("RETURN STATUS :", order.returnStatus);
        console.log("RETURN REASON :", order.returnReason);
        console.log("=================================");

        displayOrder(order);
    })

    .catch(error => {

        console.error(
            "ORDER ERROR :",
            error
        );

        showError(
            "Unable to load order"
        );
    });
}


// =====================================
// GET ORDER STATUS
// =====================================

function getOrderStatus(order) {

    const status =
        order.orderStatus ||
        order.status ||
        "PENDING";

    return status
        .toString()
        .toUpperCase();
}


// =====================================
// GET RETURN STATUS
// =====================================

function getReturnStatus(order) {

    const status =
        order.returnStatus ||
        "NONE";

    return status
        .toString()
        .toUpperCase();
}


// =====================================
// TRACKING
// =====================================

function getTrackingHTML(status) {

    status =
        (status || "PENDING")
            .toUpperCase();


    // =====================================
    // ORDERED
    // =====================================

    const ordered = [
        "PENDING",
        "CONFIRMED",
        "PROCESSING",
        "SHIPPED",
        "OUT_FOR_DELIVERY",
        "DELIVERED"
    ].includes(status);


    // =====================================
    // PROCESSING
    // =====================================

    const processing = [
        "PROCESSING",
        "SHIPPED",
        "OUT_FOR_DELIVERY",
        "DELIVERED"
    ].includes(status);


    // =====================================
    // SHIPPED
    // =====================================

    const shipped = [
        "SHIPPED",
        "OUT_FOR_DELIVERY",
        "DELIVERED"
    ].includes(status);


    // =====================================
    // DELIVERED
    // =====================================

    const delivered =
        status === "DELIVERED";


    // =====================================
    // CANCELLED
    // =====================================

    const cancelled =
        status === "CANCELLED";


    // =====================================
    // CANCELLED TRACKING
    // =====================================

    if (cancelled) {

        return `
            <div class="tracking">

                <div class="step active">
                    ✓ Ordered
                </div>

                <div class="step cancel">
                    ❌ Cancelled
                </div>

            </div>
        `;
    }


    // =====================================
    // NORMAL TRACKING
    // =====================================

    return `
        <div class="tracking">

            <div class="step ${ordered ? "active" : ""}">
                ✓ Ordered
            </div>

            <div class="step ${processing ? "active" : ""}">
                📦 Processing
            </div>

            <div class="step ${shipped ? "active" : ""}">
                🚚 Shipped
            </div>

            <div class="step ${delivered ? "active" : ""}">
                📦 Delivered
            </div>

        </div>
    `;
}


// =====================================
// STATUS COLOR
// =====================================

function getStatusClass(status) {

    status =
        (status || "PENDING")
            .toUpperCase();

    switch (status) {

        case "PENDING":
            return "placed";

        case "CONFIRMED":
            return "placed";

        case "PROCESSING":
            return "processing";

        case "SHIPPED":
            return "shipped";

        case "OUT_FOR_DELIVERY":
            return "shipped";

        case "DELIVERED":
            return "delivered";

        case "CANCELLED":
            return "cancelled";

        default:
            return "placed";
    }
}


// =====================================
// DISPLAY ORDER
// =====================================

function displayOrder(order) {

    // =====================================
    // STATUS
    // =====================================

    const orderStatus =
        getOrderStatus(order);

    const returnStatus =
        getReturnStatus(order);


    console.log(
        "FINAL ORDER STATUS :",
        orderStatus
    );

    console.log(
        "FINAL RETURN STATUS :",
        returnStatus
    );


    // =====================================
    // ORDER ITEMS
    // =====================================

    let items = "";

    const orderItems =
        order.orderItems ||
        order.items ||
        [];


    if (orderItems.length > 0) {

        orderItems.forEach(item => {

            const image =
                item.productImage ||
                item.image ||
                item.product?.image ||
                "/images/no-image.png";


            const productName =
                item.productName ||
                item.name ||
                item.product?.name ||
                "Product";


            const quantity =
                item.quantity || 1;


            const subtotal =
                item.subTotal ??
                item.subtotal ??
                item.price ??
                item.productPrice ??
                0;


            items += `
                <div class="order-product">

                    <img
                        src="${image}"
                        onerror="
                            this.src='/images/no-image.png'
                        "
                    >

                    <div class="product-info">

                        <h3>
                            ${productName}
                        </h3>

                        <p>
                            Quantity :
                            ${quantity}
                        </p>

                        <p>
                            Price :
                            ₹${subtotal}
                        </p>

                    </div>

                </div>
            `;
        });

    } else {

        items = `
            <p>
                No products found.
            </p>
        `;
    }


    // =====================================
    // ADDRESS
    // =====================================

    const address =
        order.address || {};


    const customerName =
        order.customerName ||
        address.name ||
        "";


    const city =
        order.city ||
        address.city ||
        "";


    const pincode =
        order.pincode ||
        address.pincode ||
        "";


    const phone =
        order.phone ||
        address.mobile ||
        address.phone ||
        "";


    const addressText =
        typeof order.address === "string"
            ? order.address
            : (
                address.address ||
                address.fullAddress ||
                ""
            );


    // =====================================
    // PAYMENT
    // =====================================

    const totalAmount =
        order.totalAmount ??
        order.amount ??
        order.total_amount ??
        0;


    const paymentMethod =
        order.paymentMethod ||
        order.payment_method ||
        "COD";


    const paymentStatus =
        order.paymentStatus ||
        "PENDING";


    // =====================================
    // DATE
    // =====================================

    const createdDate =
        order.createdDate ||
        order.createdAt ||
        order.orderDate;


    // =====================================
    // TRACKING
    // =====================================

    const tracking =
        getTrackingHTML(orderStatus);


    // =====================================
    // STATUS CLASS
    // =====================================

    const statusClass =
        getStatusClass(orderStatus);


    // =====================================
    // CANCEL CONDITION
    // =====================================

    const canCancel =
        (
            orderStatus === "PENDING" ||
            orderStatus === "CONFIRMED"
        );


    // =====================================
    // RETURN CONDITION
    // =====================================

    const canReturn =
        orderStatus === "DELIVERED" &&
        returnStatus === "NONE";


    console.log(
        "CAN CANCEL :",
        canCancel
    );

    console.log(
        "CAN RETURN :",
        canReturn
    );


    // =====================================
    // RETURN STATUS MESSAGE
    // =====================================

    let returnMessage = "";


    if (returnStatus === "REQUESTED") {

        returnMessage = `
            <div class="return-status requested">

                ↩ Return Request Submitted

                ${
                    order.returnReason
                        ? `
                            <div class="return-reason-display">
                                <strong>Reason:</strong>
                                ${order.returnReason}
                            </div>
                        `
                        : ""
                }

            </div>
        `;

    } else if (returnStatus === "APPROVED") {

        returnMessage = `
            <div class="return-status approved">

                ✓ Return Approved

                ${
                    order.returnReason
                        ? `
                            <div class="return-reason-display">
                                <strong>Reason:</strong>
                                ${order.returnReason}
                            </div>
                        `
                        : ""
                }

            </div>
        `;

    } else if (returnStatus === "REJECTED") {

        returnMessage = `
            <div class="return-status rejected">

                ✕ Return Request Rejected

                ${
                    order.returnReason
                        ? `
                            <div class="return-reason-display">
                                <strong>Reason:</strong>
                                ${order.returnReason}
                            </div>
                        `
                        : ""
                }

            </div>
        `;

    } else if (returnStatus === "RETURNED") {

        returnMessage = `
            <div class="return-status returned">

                ✓ Product Returned

                ${
                    order.returnReason
                        ? `
                            <div class="return-reason-display">
                                <strong>Reason:</strong>
                                ${order.returnReason}
                            </div>
                        `
                        : ""
                }

            </div>
        `;
    }


    // =====================================
    // DISPLAY
    // =====================================

    document.getElementById(
        "orderDetails"
    ).innerHTML = `

        <div class="order-container">


            <!-- ========================= -->
            <!-- ORDER HEADER -->
            <!-- ========================= -->

            <div class="order-header">

                <div>

                    <h2>
                        Order #${order.id}
                    </h2>

                    <p>
                        Placed On :
                        ${formatDate(createdDate)}
                    </p>

                </div>


                <span class="status ${statusClass}">
                    ${orderStatus}
                </span>

            </div>


            <!-- ========================= -->
            <!-- TRACKING -->
            <!-- ========================= -->

            ${tracking}


            <!-- ========================= -->
            <!-- PRODUCTS -->
            <!-- ========================= -->

            <div class="box">

                <h2>
                    Products
                </h2>

                ${items}

            </div>


            <!-- ========================= -->
            <!-- PAYMENT -->
            <!-- ========================= -->

            <div class="box">

                <h2>
                    Payment Details
                </h2>


                <div class="row">

                    <span>
                        Total Amount
                    </span>

                    <b>
                        ₹${totalAmount}
                    </b>

                </div>


                <div class="row">

                    <span>
                        Payment Method
                    </span>

                    <b>
                        ${paymentMethod}
                    </b>

                </div>


                <div class="row">

                    <span>
                        Payment Status
                    </span>

                    <b>
                        ${paymentStatus}
                    </b>

                </div>

            </div>


            <!-- ========================= -->
            <!-- DELIVERY ADDRESS -->
            <!-- ========================= -->

            <div class="box">

                <h2>
                    Delivery Address
                </h2>


                <p>

                    <b>
                        ${customerName}
                    </b>

                    <br>

                    ${addressText}

                    ${
                        addressText
                            ? "<br>"
                            : ""
                    }

                    ${city}

                    ${
                        pincode
                            ? ", PIN : " + pincode
                            : ""
                    }

                    ${
                        phone
                            ? "<br>Mobile : " + phone
                            : ""
                    }

                </p>

            </div>


            <!-- ========================= -->
            <!-- CANCEL ORDER -->
            <!-- ========================= -->

            ${
                canCancel
                    ? `
                        <button
                            type="button"
                            class="cancel-btn"
                            onclick="cancelOrder(${order.id})"
                        >
                            ❌ Cancel Order
                        </button>
                    `
                    : ""
            }


            <!-- ========================= -->
            <!-- RETURN ORDER -->
            <!-- ========================= -->

            ${
                canReturn
                    ? `
                        <button
                            type="button"
                            class="return-btn"
                            onclick="returnOrder(${order.id})"
                        >
                            ↩ Return Order
                        </button>
                    `
                    : ""
            }


            <!-- ========================= -->
            <!-- RETURN STATUS -->
            <!-- ========================= -->

            ${returnMessage}


            <br>
            <br>


            <!-- ========================= -->
            <!-- BACK -->
            <!-- ========================= -->

            <a
                href="my-orders.html"
                class="back-btn"
            >
                ← Back To Orders
            </a>


        </div>

    `;
}


// =====================================
// CANCEL ORDER
// =====================================

function cancelOrder(id) {

    if (
        !confirm(
            "Do you want to cancel this order?"
        )
    ) {
        return;
    }


    fetch(
        "/api/orders/cancel/" + id,
        {
            method: "PUT",

            headers: {
                "Authorization":
                    "Bearer " + token,

                "Content-Type":
                    "application/json"
            }
        }
    )

    .then(res => {

        if (res.status === 401) {

            localStorage.clear();

            window.location.href =
                "login.html";

            return;
        }


        if (!res.ok) {

            throw new Error(
                "Unable to cancel order"
            );
        }


        return res.json();
    })

    .then(order => {

        if (!order) {
            return;
        }


        alert(
            "Order Cancelled Successfully"
        );


        displayOrder(order);
    })

    .catch(error => {

        console.error(
            "CANCEL ERROR :",
            error
        );


        alert(
            error.message ||
            "Unable to cancel order"
        );
    });
}


// =====================================
// RETURN ORDER
// =====================================

function returnOrder(id) {

    // IMPORTANT:
    // Only open the reason modal.
    // Do NOT call API here.

    showReturnReasonModal(id);
}


// =====================================
// SHOW RETURN REASON MODAL
// =====================================

function showReturnReasonModal(id) {

    // =====================================
    // REMOVE OLD MODAL
    // =====================================

    const oldModal =
        document.getElementById(
            "returnReasonModal"
        );


    if (oldModal) {
        oldModal.remove();
    }


    // =====================================
    // CREATE MODAL
    // =====================================

    const modal =
        document.createElement("div");


    modal.id =
        "returnReasonModal";


    modal.className =
        "return-modal-overlay";


    modal.innerHTML = `

        <div class="return-modal">


            <!-- ========================= -->
            <!-- HEADER -->
            <!-- ========================= -->

            <div class="return-modal-header">

                <h2>
                    Return Order
                </h2>


                <button
                    type="button"
                    class="return-close-btn"
                    onclick="closeReturnReasonModal()"
                >
                    ×
                </button>

            </div>


            <!-- ========================= -->
            <!-- BODY -->
            <!-- ========================= -->

            <div class="return-modal-body">

                <h3>
                    Why do you want to return this order?
                </h3>


                <p class="return-help-text">

                    Please select a reason for returning
                    your order.

                </p>


                <!-- ========================= -->
                <!-- REASONS -->
                <!-- ========================= -->

                <div class="return-reasons">


                    <label class="return-reason">

                        <input
                            type="radio"
                            name="returnReason"
                            value="Wrong product received"
                        >

                        <span>
                            📦 Wrong product received
                        </span>

                    </label>


                    <label class="return-reason">

                        <input
                            type="radio"
                            name="returnReason"
                            value="Product is damaged"
                        >

                        <span>
                            💥 Product is damaged
                        </span>

                    </label>


                    <label class="return-reason">

                        <input
                            type="radio"
                            name="returnReason"
                            value="Product is defective / not working"
                        >

                        <span>
                            ⚠️ Product is defective /
                            not working
                        </span>

                    </label>


                    <label class="return-reason">

                        <input
                            type="radio"
                            name="returnReason"
                            value="Product is different from description"
                        >

                        <span>
                            📝 Product is different
                            from description
                        </span>

                    </label>


                    <label class="return-reason">

                        <input
                            type="radio"
                            name="returnReason"
                            value="Wrong size / fit"
                        >

                        <span>
                            📏 Wrong size / fit
                        </span>

                    </label>


                    <label class="return-reason">

                        <input
                            type="radio"
                            name="returnReason"
                            value="Received different product"
                        >

                        <span>
                            🔄 Received different product
                        </span>

                    </label>


                    <label class="return-reason">

                        <input
                            type="radio"
                            name="returnReason"
                            value="Product not as expected"
                        >

                        <span>
                            😕 Product not as expected
                        </span>

                    </label>


                    <label class="return-reason">

                        <input
                            type="radio"
                            name="returnReason"
                            value="Other reason"
                        >

                        <span>
                            📝 Other reason
                        </span>

                    </label>


                </div>

            </div>


            <!-- ========================= -->
            <!-- FOOTER -->
            <!-- ========================= -->

            <div class="return-modal-footer">

                <button
                    type="button"
                    class="return-cancel-btn"
                    onclick="closeReturnReasonModal()"
                >
                    Cancel
                </button>


                <button
                    type="button"
                    class="return-continue-btn"
                    onclick="submitReturnRequest(${id})"
                >
                    Continue Return
                </button>

            </div>


        </div>

    `;


    document.body.appendChild(modal);
}


// =====================================
// CLOSE RETURN MODAL
// =====================================

function closeReturnReasonModal() {

    const modal =
        document.getElementById(
            "returnReasonModal"
        );


    if (modal) {
        modal.remove();
    }
}


// =====================================
// SUBMIT RETURN REQUEST
// =====================================

async function submitReturnRequest(id) {

    console.log("=================================");
    console.log("RETURN ORDER ID :", id);


    // =====================================
    // GET SELECTED REASON
    // =====================================

    const selectedReason =
        document.querySelector(
            'input[name="returnReason"]:checked'
        );


    // =====================================
    // VALIDATE REASON
    // =====================================

    if (!selectedReason) {

        alert(
            "Please select a return reason."
        );

        return;
    }


    const reason =
        selectedReason.value.trim();


    console.log(
        "RETURN REASON :",
        reason
    );


    if (!reason) {

        alert(
            "Please select a valid return reason."
        );

        return;
    }


    // =====================================
    // TOKEN CHECK
    // =====================================

    const currentToken =
        localStorage.getItem("token");


    if (!currentToken) {

        alert(
            "Please login again."
        );

        localStorage.clear();

        window.location.href =
            "login.html";

        return;
    }


    // =====================================
    // REQUEST BODY
    // =====================================

    const requestBody = {

        returnReason:
            reason
    };


    console.log(
        "RETURN REQUEST BODY :",
        requestBody
    );


    console.log(
        "RETURN JSON :",
        JSON.stringify(requestBody)
    );


    // =====================================
    // DISABLE BUTTON
    // =====================================

    const continueButton =
        document.querySelector(
            ".return-continue-btn"
        );


    if (continueButton) {

        continueButton.disabled = true;

        continueButton.textContent =
            "Submitting...";
    }


    try {

        // =====================================
        // API CALL
        // =====================================

        const response =
            await fetch(
                "/api/orders/return/" + id,
                {
                    method: "PUT",

                    headers: {

                        "Authorization":
                            "Bearer " + currentToken,

                        "Content-Type":
                            "application/json",

                        "Accept":
                            "application/json"
                    },

                    body:
                        JSON.stringify(
                            requestBody
                        )
                }
            );


        // =====================================
        // 401
        // =====================================

        if (response.status === 401) {

            localStorage.clear();

            window.location.href =
                "login.html";

            return;
        }


        // =====================================
        // RESPONSE TEXT
        // =====================================

        const responseText =
            await response.text();


        console.log(
            "RETURN RESPONSE STATUS :",
            response.status
        );


        console.log(
            "RETURN RESPONSE :",
            responseText
        );


        // =====================================
        // ERROR
        // =====================================

        if (!response.ok) {

            let errorMessage =
                "Unable to submit return request";


            try {

                const errorData =
                    JSON.parse(
                        responseText
                    );


                errorMessage =
                    errorData.message ||
                    errorData.error ||
                    responseText ||
                    errorMessage;

            } catch (e) {

                if (responseText) {

                    errorMessage =
                        responseText;
                }
            }


            throw new Error(
                errorMessage
            );
        }


        // =====================================
        // SUCCESS RESPONSE
        // =====================================

        let order;


        try {

            order =
                JSON.parse(
                    responseText
                );

        } catch (e) {

            console.error(
                "INVALID JSON RESPONSE :",
                responseText
            );

            throw new Error(
                "Invalid server response"
            );
        }


        // =====================================
        // CLOSE MODAL
        // =====================================

        closeReturnReasonModal();


        // =====================================
        // SUCCESS MESSAGE
        // =====================================

        alert(
            "✅ Return Request Submitted Successfully"
        );


        // =====================================
        // UPDATE PAGE
        // =====================================

        displayOrder(order);


        console.log(
            "RETURN REQUEST SUCCESS :",
            order
        );

    } catch (error) {

        console.error(
            "RETURN ERROR :",
            error
        );


        alert(
            error.message ||
            "Unable to submit return request"
        );


        // =====================================
        // ENABLE BUTTON AGAIN
        // =====================================

        if (continueButton) {

            continueButton.disabled =
                false;

            continueButton.textContent =
                "Continue Return";
        }
    }
}


// =====================================
// DATE FORMAT
// =====================================

function formatDate(date) {

    if (!date) {
        return "-";
    }


    return new Date(date)
        .toLocaleString(
            "en-IN",
            {
                day: "2-digit",
                month: "short",
                year: "numeric",
                hour: "2-digit",
                minute: "2-digit"
            }
        );
}


// =====================================
// ERROR
// =====================================

function showError(message) {

    document.getElementById(
        "orderDetails"
    ).innerHTML = `

        <div class="error-box">

            <h2>
                ${message}
            </h2>

            <br>

            <a
                href="my-orders.html"
                class="back-btn"
            >
                ← Back To Orders
            </a>

        </div>

    `;
}