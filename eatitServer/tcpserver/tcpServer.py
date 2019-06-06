import socket, threading
import sys


class TCPServer:
    def __init__(self, HOST, PORT):
        self.HOST = HOST
        self.PORT = PORT

        self.s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.s.bind((self.HOST, self.PORT))
        self.s.listen(1)

    def run(self):
        # Bind socket to host and port

        try:
            while True:
                print('Server wait...')
                connection, clntAddr = self.s.accept()
                print('Connect with ', clntAddr[0], ':', str(clntAddr[1]))
                try:
                    while True:
                        # data = connection.recv(64).decode()
                        data = connection.recv(64)
                        if not data:
                            print('TCP server :: exit :', connection)
                            break
                        print('TCP server :: client : ', data)
                except:
                    exit(0)
        except:
            print('TCP server :: serverThread error')
        self.s.close()
