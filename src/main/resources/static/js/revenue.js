/* =========================================================
   MANIKANTA SALES
   REVENUE DASHBOARD JS
========================================================= */

const API_URL = "/api/orders";

let revenueChart = null;


/* =========================================================
   PAGE LOAD
========================================================= */

document.addEventListener("DOMContentLoaded", () => {

    loadRevenue();

    loadOrders();

    loadChart();


    const chartDropdown =
        document.getElementById("chartType");

    if (chartDropdown) {

        chartDropdown.addEventListener("change", () => {

            loadChart();

        });

    }

});


/* =========================================================
   GET TOKEN
========================================================= */

function getToken() {

    return localStorage.getItem("token");

}


/* =========================================================
   COMMON HEADERS
========================================================= */

function getHeaders() {

    const token = getToken();

    return {

        "Authorization": "Bearer " + token,

        "Content-Type": "application/json",

        "Accept": "application/json"

    };

}


/* =========================================================
   CHECK LOGIN
========================================================= */

function checkAuthentication() {

    const token = getToken();

    if (!token) {

        console.warn(
            "No authentication token found."
        );

        return false;

    }

    return true;

}


/* =========================================================
   LOAD REVENUE SUMMARY
========================================================= */

async function loadRevenue() {

    if (!checkAuthentication()) {
        return;
    }


    try {

        const response = await fetch(

            API_URL + "/revenue",

            {

                method: "GET",

                headers: getHeaders()

            }

        );


        /* =========================
           UNAUTHORIZED
        ========================== */

        if (response.status === 401) {

            handleUnauthorized();

            return;

        }


        if (!response.ok) {

            throw new Error(
                "Revenue API Failed : " +
                response.status
            );

        }


        const data =
            await response.json();


        console.log(
            "Revenue Data:",
            data
        );


        /* =========================
           TOTAL REVENUE
        ========================== */

        const totalRevenue =
            data.totalRevenue ?? 0;


        setText(

            "totalRevenue",

            "₹ " +
            formatMoney(totalRevenue)

        );


        /* =========================
           TODAY REVENUE
        ========================== */

        const todayRevenue =
            data.todayRevenue ?? 0;


        setText(

            "todayRevenue",

            "₹ " +
            formatMoney(todayRevenue)

        );


        /* =========================
           MONTH REVENUE
        ========================== */

        const monthRevenue =
            data.monthRevenue ?? 0;


        setText(

            "monthRevenue",

            "₹ " +
            formatMoney(monthRevenue)

        );


        /* =========================
           TOTAL ORDERS
        ========================== */

        const totalOrders =
            data.totalOrders ?? 0;


        setText(

            "totalOrders",

            totalOrders

        );


        /* =========================
           AVERAGE ORDER VALUE
        ========================== */

        let averageOrder =
            data.averageOrderValue;


        /*
           If backend doesn't send
           averageOrderValue,
           calculate it here.
        */

        if (
            averageOrder === null ||
            averageOrder === undefined
        ) {

            if (Number(totalOrders) > 0) {

                averageOrder =
                    Number(totalRevenue) /
                    Number(totalOrders);

            } else {

                averageOrder = 0;

            }

        }


        setText(

            "averageOrder",

            "₹ " +
            formatMoney(averageOrder)

        );


    } catch (error) {

        console.error(
            "Revenue Error:",
            error
        );

    }

}


/* =========================================================
   LOAD RECENT ORDERS
========================================================= */

async function loadOrders() {

    if (!checkAuthentication()) {
        return;
    }


    const table =
        document.getElementById("orderTable");


    if (!table) {
        return;
    }


    try {

        const response = await fetch(

            API_URL + "/recent",

            {

                method: "GET",

                headers: getHeaders()

            }

        );


        /* =========================
           UNAUTHORIZED
        ========================== */

        if (response.status === 401) {

            handleUnauthorized();

            return;

        }


        if (!response.ok) {

            throw new Error(

                "Recent Orders API Failed : " +
                response.status

            );

        }


        const orders =
            await response.json();


        console.log(
            "Recent Orders:",
            orders
        );


        table.innerHTML = "";


        /* =========================
           NO ORDERS
        ========================== */

        if (
            !orders ||
            !Array.isArray(orders) ||
            orders.length === 0
        ) {

            table.innerHTML = `

                <tr>

                    <td
                        colspan="6"
                        style="
                            text-align:center;
                            padding:30px;
                            color:#777;
                        "
                    >
                        No orders found
                    </td>

                </tr>

            `;


            updatePaymentStatus([]);

            return;

        }


        /* =========================
           RENDER ORDERS
        ========================== */

        orders.forEach(order => {


            const orderId =
                order.id ?? "-";


            const customerName =
                getCustomerName(order);


            const amount =
                getOrderAmount(order);


            const paymentMethod =
                getPaymentMethod(order);


            const paymentStatus =
                getPaymentStatus(order);


            const orderStatus =
                getOrderStatus(order);


            const orderDate =
                getOrderDate(order);


            const row =
                document.createElement("tr");


            row.innerHTML = `

                <!-- ORDER ID -->

                <td>

                    <strong>
                        #${escapeHTML(orderId)}
                    </strong>

                </td>


                <!-- CUSTOMER -->

                <td>

                    ${escapeHTML(customerName)}

                </td>


                <!-- AMOUNT -->

                <td>

                    <strong>
                        ₹ ${formatMoney(amount)}
                    </strong>

                </td>


                <!-- PAYMENT -->

                <td>

                    <span class="payment-method">

                        ${escapeHTML(paymentMethod)}

                    </span>

                    <br>

                    <small class="
                        payment-status
                        ${getStatusClass(paymentStatus)}
                    ">

                        ${escapeHTML(
                            formatStatus(paymentStatus)
                        )}

                    </small>

                </td>


                <!-- ORDER STATUS -->

                <td>

                    <span class="
                        status
                        ${getStatusClass(orderStatus)}
                    ">

                        ${formatStatus(orderStatus)}

                    </span>

                </td>


                <!-- DATE -->

                <td>

                    ${escapeHTML(orderDate)}

                </td>

            `;


            table.appendChild(row);

        });


        /* =========================
           PAYMENT STATUS CARDS
        ========================== */

        updatePaymentStatus(orders);


    } catch (error) {

        console.error(
            "Orders Error:",
            error
        );


        table.innerHTML = `

            <tr>

                <td
                    colspan="6"
                    style="
                        text-align:center;
                        padding:30px;
                        color:#d32f2f;
                    "
                >
                    Failed to load orders
                </td>

            </tr>

        `;

    }

}


/* =========================================================
   LOAD REVENUE CHART
========================================================= */

async function loadChart() {

    if (!checkAuthentication()) {
        return;
    }


    const dropdown =
        document.getElementById("chartType");


    let type = "daily";


    if (dropdown) {

        type =
            dropdown.value || "daily";

    }


    try {

        const response = await fetch(

            API_URL +
            "/chart?type=" +
            encodeURIComponent(type),

            {

                method: "GET",

                headers: getHeaders()

            }

        );


        /* =========================
           UNAUTHORIZED
        ========================== */

        if (response.status === 401) {

            handleUnauthorized();

            return;

        }


        if (!response.ok) {

            throw new Error(

                "Chart API Failed : " +
                response.status

            );

        }


        const data =
            await response.json();


        console.log(
            "Chart Data:",
            data
        );


        /* =========================
           CHART DATA
        ========================== */

        const labels =
            Array.isArray(data.labels)
                ? data.labels
                : [];


        const values =
            Array.isArray(data.values)
                ? data.values
                : [];


        updateChart(

            labels,

            values,

            type

        );


    } catch (error) {

        console.error(
            "Chart Error:",
            error
        );


        updateChart(

            [],

            [],

            type

        );

    }

}


/* =========================================================
   UPDATE CHART
========================================================= */

function updateChart(

    labels,

    values,

    type

) {

    const canvas =
        document.getElementById(
            "revenueChart"
        );


    if (!canvas) {
        return;
    }


    const ctx =
        canvas.getContext("2d");


    /* =========================
       DESTROY OLD CHART
    ========================== */

    if (revenueChart) {

        revenueChart.destroy();

        revenueChart = null;

    }


    /* =========================
       CREATE NEW CHART
    ========================== */

    revenueChart =
        new Chart(

            ctx,

            {

                type: "line",


                data: {

                    labels: labels,


                    datasets: [

                        {

                            label:
                                getChartLabel(type),

                            data: values,

                            borderWidth: 3,

                            tension: 0.4,

                            fill: true,

                            pointRadius: 4,

                            pointHoverRadius: 6

                        }

                    ]

                },


                options: {

                    responsive: true,

                    maintainAspectRatio: false,


                    interaction: {

                        intersect: false,

                        mode: "index"

                    },


                    scales: {

                        y: {

                            beginAtZero: true,


                            ticks: {

                                callback:
                                    function(value) {

                                        return (
                                            "₹ " +
                                            formatMoney(value)
                                        );

                                    }

                            }

                        }

                    },


                    plugins: {

                        legend: {

                            display: true

                        },


                        tooltip: {

                            callbacks: {

                                label:
                                    function(context) {

                                        return (

                                            " Revenue: ₹ " +

                                            formatMoney(
                                                context.raw
                                            )

                                        );

                                    }

                            }

                        }

                    }

                }

            }

        );

}


/* =========================================================
   PAYMENT STATUS CARDS
========================================================= */

function updatePaymentStatus(orders) {

    let paid = 0;

    let pending = 0;

    let refunded = 0;


    /* =========================
       COUNT PAYMENT STATUS
    ========================== */

    if (Array.isArray(orders)) {

        orders.forEach(order => {

            const status =
                getPaymentStatus(order)
                    .toString()
                    .toUpperCase()
                    .trim();


            if (status === "PAID") {

                paid++;

            }


            else if (status === "PENDING") {

                pending++;

            }


            else if (status === "REFUNDED") {

                refunded++;

            }

        });

    }


    /* =========================
       PAID CARD
    ========================== */

    const paidElement =
        document.querySelector(
            ".paid-card .paid"
        );


    /* =========================
       PENDING CARD
    ========================== */

    const pendingElement =
        document.querySelector(
            ".pending-card .pending"
        );


    /* =========================
       REFUNDED CARD
    ========================== */

    const refundedElement =
        document.querySelector(
            ".refunded-card .refunded"
        );


    /* =========================
       UPDATE VALUES
    ========================== */

    if (paidElement) {

        paidElement.textContent =
            paid;

    }


    if (pendingElement) {

        pendingElement.textContent =
            pending;

    }


    if (refundedElement) {

        refundedElement.textContent =
            refunded;

    }

}


/* =========================================================
   CUSTOMER NAME
========================================================= */

function getCustomerName(order) {

    return (

        order.customerName ||

        order.customer_name ||

        order.userName ||

        order.user?.name ||

        order.user?.firstName ||

        order.customer?.name ||

        order.userEmail ||

        order.user_email ||

        "-"

    );

}


/* =========================================================
   ORDER AMOUNT
========================================================= */

function getOrderAmount(order) {

    return (

        order.amount ??

        order.totalAmount ??

        order.total_amount ??

        order.total ??

        0

    );

}


/* =========================================================
   PAYMENT METHOD
========================================================= */

function getPaymentMethod(order) {

    return (

        order.paymentMethod ||

        order.payment_method ||

        "COD"

    );

}


/* =========================================================
   PAYMENT STATUS
========================================================= */

function getPaymentStatus(order) {

    return (

        order.paymentStatus ||

        order.payment_status ||

        "PENDING"

    );

}


/* =========================================================
   ORDER STATUS
========================================================= */

function getOrderStatus(order) {

    return (

        order.orderStatus ||

        order.order_status ||

        order.status ||

        "PENDING"

    );

}


/* =========================================================
   ORDER DATE
========================================================= */

function getOrderDate(order) {

    const date =

        order.createdDate ||

        order.createdAt ||

        order.orderDate ||

        order.date;


    if (!date) {

        return "-";

    }


    try {

        const parsedDate =
            new Date(date);


        if (
            Number.isNaN(
                parsedDate.getTime()
            )
        ) {

            return date;

        }


        return parsedDate.toLocaleDateString(

            "en-IN",

            {

                day: "2-digit",

                month: "short",

                year: "numeric"

            }

        );


    } catch (error) {

        return date;

    }

}


/* =========================================================
   CHART LABEL
========================================================= */

function getChartLabel(type) {

    if (type === "weekly") {

        return "Weekly Revenue";

    }


    if (type === "monthly") {

        return "Monthly Revenue";

    }


    return "Daily Revenue";

}


/* =========================================================
   FORMAT STATUS
========================================================= */

function formatStatus(status) {

    if (!status) {

        return "Pending";

    }


    return status

        .toString()

        .replaceAll("_", " ")

        .toLowerCase()

        .replace(
            /\b\w/g,
            char => char.toUpperCase()
        );

}


/* =========================================================
   STATUS CSS CLASS
========================================================= */

function getStatusClass(status) {

    if (!status) {

        return "pending";

    }


    return status

        .toString()

        .toLowerCase()

        .replaceAll(" ", "_");

}


/* =========================================================
   FORMAT MONEY
========================================================= */

function formatMoney(value) {

    if (

        value === null ||

        value === undefined ||

        value === ""

    ) {

        return "0";

    }


    const number =
        Number(value);


    if (Number.isNaN(number)) {

        return "0";

    }


    return number.toLocaleString(

        "en-IN",

        {

            maximumFractionDigits: 2

        }

    );

}


/* =========================================================
   SET TEXT
========================================================= */

function setText(

    elementId,

    value

) {

    const element =
        document.getElementById(
            elementId
        );


    if (element) {

        element.textContent =
            value;

    }

}


/* =========================================================
   ESCAPE HTML
========================================================= */

function escapeHTML(value) {

    if (

        value === null ||

        value === undefined

    ) {

        return "";

    }


    return String(value)

        .replaceAll("&", "&amp;")

        .replaceAll("<", "&lt;")

        .replaceAll(">", "&gt;")

        .replaceAll('"', "&quot;")

        .replaceAll("'", "&#039;");

}


/* =========================================================
   UNAUTHORIZED
========================================================= */

function handleUnauthorized() {

    console.warn(
        "Session expired or unauthorized."
    );


    /*
       Uncomment if you want
       automatic redirect.
    */

    // localStorage.removeItem("token");
    // window.location.href = "/login.html";

}