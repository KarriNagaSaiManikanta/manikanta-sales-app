// ==========================================
// MANIKANTA SALES
// FOOTER COMPONENT
// ==========================================


document.addEventListener(
    "DOMContentLoaded",
    function(){


        const footer =
            document.getElementById(
                "footer"
            );



        if(!footer){
            return;
        }




        footer.innerHTML = `

        <div class="footer-container">


            <div class="footer-section">

                <h3>
                    Manikanta Sales
                </h3>

                <p>
                    Your trusted online shopping platform.
                </p>

            </div>





            <div class="footer-section">

                <h3>
                    Quick Links
                </h3>

                <a href="/index.html">
                    Home
                </a>

                <a href="/products.html">
                    Products
                </a>

                <a href="/cart.html">
                    Cart
                </a>

                <a href="/contact.html">
                    Contact
                </a>

            </div>





            <div class="footer-section">

                <h3>
                    Contact
                </h3>


                <p>
                    Email:
                    karripawan86@gmail.com
                </p>


                <p>
                    Phone:
                    +91 XXXXX XXXXX
                </p>


            </div>



        </div>



        <div class="footer-bottom">

            © 2026 Manikanta Sales.
            All Rights Reserved.

        </div>

        `;



    }
);