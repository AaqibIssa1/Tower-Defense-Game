//var socket = io.connect("http://205.237.185.214:9182");
var socket = io.connect("coms-309-sr-2.misc.iastate.edu:9128");
//var socket = io.connect("http://127.0.0.1:88");

var usname = document.getElementById("_username");
var pass = document.getElementById("_password");
var button = document.getElementById("_btn");

button.addEventListener('click', function() {
    socket.emit('registerAttempt', {
        username: usname.value,
        password: pass.value
    });
    console.log("Clicked");
    window.location = "../index.html";
})