import threading
from saveEval import SaveEval
from ml_v2 import DNN
from recommend import Recommend

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
                elif datastr == 'train':
                    self.train()
                elif datastr == 'recommend':
                    self.recommend()
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

        uid = int(self.getdata(64))
        if not uid:
            return
        # print("uid: ", uid)

        fid = int(self.getdata(16))
        if not fid:
            return
        # print("fid: ", fid)

        y = []
        while True:
            data = self.getdata(16)
            if not data:
                return
            if str(data.decode()) == 'evalEnd':
                break
            y.append(int(data))
        # print('y: ', y)

        save = SaveEval(uid, fid, y)
        save.run()
        print('</get eval>')
        print()

    def train(self):
        print()
        print('<train>')

        uid = int(self.getdata(64))
        if not uid:
            return
        print("uid: ", uid)

        dnn = DNN(uid)
        dnn.train()

        response = 'ok\n'
        self.connection.sendall(response.encode())

        print('</train>')
        print()

    def recommend(self):
        print()
        print('<recommend>')

        uid = int(self.getdata(64))
        if not uid:
            return
        print("uid: ", uid)

        recommend = Recommend(uid)
        recommend.run()
        print(recommend.items)

        for i in range(10):
            data = str(recommend.items[i]) + '\n'
            self.connection.sendall(data.encode())

        print('</recommend>')
        print()

