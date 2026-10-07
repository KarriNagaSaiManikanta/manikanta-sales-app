// ================================
// THEME JS
// ================================


document.addEventListener("DOMContentLoaded",()=>{


    const themeToggle =
    document.getElementById("themeToggle");



    // Load saved theme

    let theme =
    localStorage.getItem("theme");



    if(theme === "dark"){

        document.body.classList.add("dark");

        if(themeToggle){

            themeToggle.checked = true;

        }

    }






    // Change Theme

    if(themeToggle){


        themeToggle.addEventListener("change",()=>{


            if(themeToggle.checked){


                document.body.classList.add("dark");

                localStorage.setItem(
                    "theme",
                    "dark"
                );


            }
            else{


                document.body.classList.remove("dark");

                localStorage.setItem(
                    "theme",
                    "light"
                );


            }


        });


    }



});