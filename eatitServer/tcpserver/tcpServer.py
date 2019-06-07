import socket, threading
import sys


class TCPServer:
    def __init__(self, HOST, PORT):
        self.HOST = HOST
        self.PORT = PORT

        self.s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.s.bind((self.HOST, self.PORT))
        self.s.listen(1)
        self.connection = None

    def run(self):
        # Bind socket to host and port

        try:
            while True:
                print('Server wait...')
                self.connection, clntAddr = self.s.accept()
                print('Connect with ', clntAddr[0], ':', str(clntAddr[1]))
                try:
                    while True:
                        # data = self.connection.recv(64)
                        data = self.getdata(64)
                        if not data:
                            # print('TCP server :: exit :', self.connection)
                            break
                        print('TCP server :: client : ', data)
                        # print(str(data.decode()))
                        # print(int.from_bytes(data[0:4], byteorder='big'))
                        # print(int.from_bytes(data[4:8], byteorder='big'))

                        if str(data.decode()) == 'eval':
                            self.geteval()
                except:
                    exit(0)
        except:
            print('TCP server :: serverThread error')
        self.s.close()

    def geteval(self):
        print()
        print('<get eval>')

        uid = self.getdata(64)
        if not uid:
            return
        print("uid: ", int(uid))

        evaldata = self.getdata(64)
        if not evaldata:
            return
        print('evaldata: ', int(evaldata))
        print()

    def getdata(self, size):
        data = self.connection.recv(size)
        if not data:
            print('TCP server :: exit :', self.connection)
            return None
        return data
