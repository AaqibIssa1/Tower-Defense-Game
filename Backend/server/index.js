var express = require('express');
var socket = require('socket.io');
var mysql = require('mysql2');

var port = 9128;

var counter = 0;

//mysql connection
var connection = mysql.createConnection({
    host: 'coms-309-sr-2.misc.iastate.edu',
    user: 'bs_user',
    password: 'Binaya_1',
    database: 'test',
});

connection.connect(function(err) {
    if (err) {
        console.error('error connecting: ' + err.stack);
        return;
    }

    console.log('connected as id ' + connection.threadId);

    sql = "CREATE TABLE IF NOT EXISTS users (userId INT, username VARCHAR(100), password VARCHAR(100));"
    connection.query(sql, function(error) {
        if(error) {
            throw error;
        }
    });

    sql = "CREATE TABLE IF NOT EXISTS counter (userIdCounter INT);"
    connection.query(sql, function(error) {
        if(error) {
            throw error;
        }
    });

    sql = "select userIdCounter from counter;"
    connection.query(sql, function(error, data) {
        if(error) {
            throw error;
        }
        if(data[0]) {
            counter = data[0].userIdCounter;
            console.log("data: " + data[0].userIdCounter);
        } else {
            sql = "insert into counter (userIdCounter) values (0);"
            connection.query(sql, function(error) {
                if(error) {
                    throw error;
                }
                console.log("init. counter");
            });
        }
    })

});


//app setup
var app = express();
var server = app.listen(port, function() {
    console.log('listening to requests on port ' + port);
})

//Static files
app.use(express.static('public'));

//Socket setup
var io = socket(server);

io.on('connection', function(socket) {
    console.log('connected user: ' + socket.id);

    socket.on('loginAttempt', function(object) {
        sql = "select password from users where username = '" + object.username + "';";
        connection.query(sql, function(error, data) {

            if(error) {
                throw error;
            } 
            
            for(let i = 0; i < data.length; i++) {
                if(object.password == data[i].password) {
                    console.log("User: " + object.username + " logged in");

                    socket.emit('loggedIn', {
                        info: "correct",
                    });

                    return;
                } 
            }
            
            console.log("Incorrect Password/User Not Found");
        });

    });

    socket.on('registerAttempt', function(object) {
        sql = "select useriD from users where username = '" + object.username + "';";
        connection.query(sql, function(error1, data) {
            if(error1) {
                throw error1;
            }

            if(data.length <= 0) {
                sql = "insert into users (userId, username, password) values (" + counter + ", '" + object.username + "', '" + object.password + "');"
                counter++;
                connection.query(sql, function(error) {
                    if(error) {
                        throw error;
                    }
                    console.log("Added user: " + object.username);

                    sql = "update counter set userIdCounter = " + counter + ";"
                    connection.query(sql, function(error) {
                        if(error) {
                            throw error;
                        }
                    });
                });
            } else {
                console.log("User Exists");
            }
        });
    });

    socket.on('requestUsers', function(object) {
        sql = "select username from users;"
        connection.query(sql, function(error, data) {
            if(error) {
                throw error;
            }
            io.sockets.emit('requestUsers', data);
        });
    })
});

