window.onload = () =>{
    document.getElementById("submit").addEventListener("click", (event) => {

        // Output
        let consoleHTML = document.getElementById("console");

        event.preventDefault();

        // Input
        codeText = document.getElementById("code").value;
        consoleHTML.value = "Compiling...\n";

        // server.js
        fetch("/code",{
          method: 'POST',
          headers: {
            "Content-Type": "application/json"
          },
          body: JSON.stringify({code: codeText})
        })
        .then(response => response.json())
        .then(data => {
          let result = data;
          console.log(data)
          consoleHTML.value += result.compiled;
        })
    });
}
