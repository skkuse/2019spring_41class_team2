import socket, threading

class TCPServerThread(threading.Thread):
    def __init__(self, tcpServerThreads, connections, connection, clntAddr):
        threading.Thread.__init__(self)

        self.tcpServerThreads = tcpServerThreads
        self.connections = connections
        self.connection = connection
        self.clntAddr = clntAddr

    def run(self):
        try:
            while True:
                data = self.connection.recv(64)
                if not data:
                    print('TCP server :: exit :', self.connection)
                    break
                print('TCP server :: client : ', data)
                # print(str(data.decode()))
                # print(int.from_bytes(data[0:4], byteorder='big'))
                # print(int.from_bytes(data[4:8], byteorder='big'))

                if str(data.decode()) == 'eval':
                    self.geteval()
        except:
            self.connections.remove(self.connection)
            self.tcpServerThreads.remove(self)
            exit(0)
        self.connections.remove(self.connection)
        self.tcpServerThreads.remove(self)

    def geteval(self):
        print()
        print('get eval')

        # user id 받기
        uid = self.connection.recv(64)
        if not uid:
            print('TCP server :: exit :', self.connection)
            return
        uid = int(uid)
        print("uid: ", uid)

        # 취향 정보 받기(테스트)
        data = self.connection.recv(64)
        if not data:
            print('TCP server :: exit :', self.connection)
            return
        print("evaldata: ", data)

        print()
