import socket, threading
import tcpServerThread
import sys


class TCPServer:
    def __init__(self, HOST, PORT):
        self.HOST = HOST
        self.PORT = PORT

        self.s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.s.bind((self.HOST, self.PORT))
        self.s.listen(1)

        self.connections = []
        self.tcpServerThreads = []

    def run(self):
        # Bind socket to host and port

        try:
            while True:
                print('Server wait...')
                connection, clntAddr = self.s.accept()
                self.connections.append(connection)
                print()
                print('Connect with ', clntAddr[0], ':', str(clntAddr[1]))

                subThread = tcpServerThread.TCPServerThread(self.tcpServerThreads, self.connections, connection, clntAddr)
                subThread.start()
                self.tcpServerThreads.append(subThread)
        except:
            print('TCP server :: serverThread error')
        self.s.close()

    def sendAll(self, message):
        try:
            pass
        except:
            pass

