var app = require('express')();
const http = require('http').Server(app);
const port = process.env.PORT||8001 // setting the port
var videojuegosActivos = {}; 
var sensoresOnlineActivos = {}; 

var usuarioPadre = {}; 
const io = require('socket.io')(http);
var contador = 0;
process.title = process.argv[2];

const desktopRoom = 'RoomOfBFApp'


app.get("/",function(req,res){
	res.sendFile(__dirname+"/index.html");
})



//Socket setup

io.on('connection', function(socket) {
	console.log("User connected");
	console.log(socket.rooms)

	socket.on("join_BG", ({room,name})=>{
		socket.join(room);
		console.log('entro el BG')

		socket.to(desktopRoom).emit("join_BG",{room,name});
	});


	socket.on("join_sensor", (payload)=>{
		var properJson = JSON.parse(payload)
		console.log(properJson)
		console.log(properJson.room)
		console.log(properJson.name)


		socket.join(properJson.room);
		console.log(sensoresOnlineActivos)

		if(!(properJson.room in sensoresOnlineActivos)){
			console.log("NameOfRoom? "+ properJson.room + " Space: " + properJson.name )
			sensoresOnlineActivos[properJson.room] = properJson.name;
		} else{
			sensoresOnlineActivos[properJson.room][properJson.name] = properJson.name;
		}
		socket.emit("join_sensor",{room:properJson.room,name:properJson.name});

	});

	socket.on("join_sensor_videogame", ({room,name})=>{
		socket.join(room);
		console.log(videojuegosActivos)
		if(!(room in videojuegosActivos)){
			console.log("NameOfRoom? "+ room + " Space: " + name )
			videojuegosActivos[room] = room;
		} else{
			videojuegosActivos[room][name] = name;
		}
		
		console.log("Ok?")
		//io.sockets.in(room).emit("message",{message:"Conectado",name});
		socket.to(desktopRoom).emit("join_sensor_videogame",{room,name});
	});
	
	socket.on("join_offline_sensors", ({room,name})=>{
		for(sensor in sensoresOnlineActivos){
			socket.join(sensor);
			socket.to(sensor).emit("join_offline_sensors",{room,name});
		}
	});
	socket.on("join_offline_sensors_confirmation", ({room,name})=>{
		socket.join(room);
		socket.to(room).emit("Imessage",{message: name + " is in the same room as you",name});
		
	});
	socket.on("join_sensor_videogame_confirmation", ({room,name})=>{
		socket.join(room);
		console.log(videojuegosActivos)
		if(!(room in videojuegosActivos)){
			console.log("NameOfRoom? "+ room + " Space: " + name )
			videojuegosActivos[room] = room;
		} else{
			videojuegosActivos[room][name] = name;
		}
		
		console.log("Ok?")
		//io.sockets.in(room).emit("message",{message:"Conectado",name});
		socket.to(room).emit("Imessage",{message:"Desktop App in the same room as you",name});
	});


	socket.on("leave_sensor", ({room,name})=>{
		socket.join(room);
		if((room in videojuegosActivos)){
			console.log("name room? "+ room + " Name: " + name )
			delete videojuegosActivos[room];
		} else{
			delete videojuegosActivos[room][name];
		}
		
		console.log("Se borró con exito")
	});
	socket.on("Omessage",({message})=> {
		console.log("message: " + message)
		for(sensor in videojuegosActivos){
			socket.to(sensor).emit("Omessage",{
				message
			});
		}
		
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
	socket.on("Dimessage",({room,message,name})=> {
		console.log("Entra al mensaje?!" + room + " CACA " + name)
		console.log("Entro lo siguiente: " + message)
		socket.to(room).emit("Dimessage",{
			message,
			name
		});
	}); 

	socket.on("AllSensors",function (){
		console.log("Enter in All Sensors")
		socket.emit("AllSensors",{videojuegosActivos});
	})
	socket.on('npmStop', () => {
		process.abort();
	  });
});

http.listen(port, function() {
	console.log('Server running in http://localhost:' + port);
});

