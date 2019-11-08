const express = require('express');
const app = express();
const server = require('http').Server(app);
const port = process.env.PORT||8000 // setting the port
var usuariosREQ = {}; 
var usuarioPadre = {}; 

server.listen(port, function() {
	console.log('Servidor corriendo en http://localhost:8000');
});


// Static files
app.use(express.static('public'));

//Socket setup
const io = require('socket.io')(server);
io.on('connection', function(socket) {
	socket.on('conectarse',function(){
		console.log('Un cliente se ha conectado y suscrito para el flujo de datos');
		if (socket.id in usuariosREQ){
			
		} else{
			usuariosREQ[socket.id] = socket.id;
			if (socket.id in usuariosREQ){
				console.log('Guardado con exito');
				console.log(usuariosREQ);
			}
		}
	})
	socket.on('connectPadre',function(){
		console.log('El cliente padre se ha conectado y suscrito para el flujo de datos');
		if (socket.id in usuarioPadre){
			
		} else{
			usuarioPadre[socket.id] = socket.id;
			if (socket.id in usuarioPadre){
				console.log('Guardado con exito');
				console.log(usuarioPadre);
			}
		}
	})
	console.log('Un cliente se ha conectado');
	console.log('ID del men: ' + socket.id);
    //socket.emit('messages', messages);
});