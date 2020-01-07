//var socket = io.connect("http://205.237.185.214:9182");
var socket = io.connect("coms-309-sr-2.misc.iastate.edu:9128");
//var socket = io.connect("http://127.0.0.1:88");

var usersList = document.getElementById("users");

socket.on("requestUsers", function(data) {
    usersList.innerHTML = "";
    for(let i = 0; i < data.length; i++) {
        usersList.append(data[i].username + "\n");
    }
});

socket.emit('requestUsers', {})