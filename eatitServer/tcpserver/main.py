import tcpServer

HOST = '192.168.219.103'
PORT = 60728

andRaspTCP = tcpServer.TCPServer(HOST, PORT)
andRaspTCP.run()
