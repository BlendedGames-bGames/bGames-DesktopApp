var app = require('express')();
const http = require('http').Server(app);
const port = process.env.PORT||8001 // setting the port
var sensoresActivos = {}; 
var usuarioPadre = {}; 
const io = require('socket.io')(http);
var contador = 0;
process.title = process.argv[2];


app.get("/",function(req,res){
	res.sendFile(__dirname+"/index.html");
})



//Socket setup

io.on('connection', function(socket) {
	console.log("User connected");

	socket.on("join_sensor", ({room,name})=>{
		socket.join(room);
		if(!(room in sensoresActivos)){
			console.log("NameOfRoom? "+ room + " Space: " + name )
			sensoresActivos[room] = room;
		} else{
			sensoresActivos[room][name] = name;
		}
		
		//console.log("Ok?")
		//io.sockets.in(room).emit("message",{message:"Conectado",name});
		socket.to(room).emit("Imessage",{message:"Connected",name});
	});

	socket.on("leave_sensor", ({room,name})=>{
		socket.join(room);
		if((room in sensoresActivos)){
			console.log("name room? "+ room + " Name: " + name )
			delete sensoresActivos[room];
		} else{
			delete sensoresActivos[room][name];
		}
		
		console.log("Se borró con exito")
	});

	socket.on("message",({room,message,name})=> {
		console.log("Name room?!" + room + " name " + name)
		console.log("message: " + message["data"])
		socket.to(room).emit("Smessage",{
			message,
			name
		});
	});
	socket.on("messageResume",({room,message,name})=> {
		console.log("Message?!" + room + " Name " + name)
		console.log("That its message: " + message["data"])
		socket.to(room).emit("Rmessage",{
			message,
			name
		});
	});
	socket.on("Emessage",({room,message,name})=> {
		console.log("Entra al mensaje?!" + room + " CACA " + name)
		console.log("Entro lo siguiente: " + message)
		socket.to(room).emit("Smessage",{
			message,
			name
		});
	}); 

	socket.on("AllSensors",function (){
		console.log("Enter in All Sensors")
		socket.emit("AllSensors",{sensoresActivos});
	})
	socket.on('npmStop', () => {
		process.abort();
	  });
});

http.listen(port, function() {
	console.log('Server running in http://localhost:' + port);
});

