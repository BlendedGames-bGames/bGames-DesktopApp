const io = require('socket.io-client');
const socketClient = io.connect('http://localhost:8001'); // Specify port if your express server is not using default port 80

socketClient.on('connect', () => {
  socketClient.emit('npmStop');
  setTimeout(() => {
    process.abort();
  }, 1000);
});