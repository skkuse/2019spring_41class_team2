import tcpServer

HOST = 'localhost'
PORT = 60728

andRaspTCP = tcpServer.TCPServer(HOST, PORT)
andRaspTCP.run()
