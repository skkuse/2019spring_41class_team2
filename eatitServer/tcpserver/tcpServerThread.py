import socket, threading
import numpy as np

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
                # print('TCP server :: client : ', data)

                datastr = str(data.decode())
                if datastr == 'eval':
                    self.geteval()
        except:
            self.connections.remove(self.connection)
            self.tcpServerThreads.remove(self)
            exit(0)
        self.connections.remove(self.connection)
        self.tcpServerThreads.remove(self)

    def getdata(self, size):
        data = self.connection.recv(size)
        if not data:
            print('TCP server :: exit :', self.connection)
            return None
        # print('data: ', data)
        return data

    def geteval(self):
        print()
        print('<get eval>')

        uid = self.getdata(64)
        if not uid:
            return
        print("uid: ", int(uid))

        fid = self.getdata(16)
        if not fid:
            return
        print("fid: ", int(fid))

        y = []
        while True:
            data = self.getdata(16)
            if not data:
                return
            if str(data.decode()) == 'evalEnd':
                break
            y.append(int(data))
        print('y: ', y)
        print('</get eval>')
        print()
