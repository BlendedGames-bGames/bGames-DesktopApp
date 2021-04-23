const io = require("socket.io-client");
console.log('aqui')
const socket = io("http://144.126.216.255:3011/");
console.log('aqui2')
socket.on("connect", function(){
    console.log("connected");
});

socket.on("disconnect", function(){
    console.log("disconnected");
});
socket.on("connect_error", (err) => {
    console.log(err)
    
});
socket.on('welcome', (msg) => {
	console.log(msg)
  });    

socket.emit("joinRoom","10");	    

socket.on('success', (msg) => {

	console.log(msg)
});     