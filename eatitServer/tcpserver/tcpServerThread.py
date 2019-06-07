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
                data = self.getdata(64)
                if not data:
                    break
                print('TCP server :: client : ', data)

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
