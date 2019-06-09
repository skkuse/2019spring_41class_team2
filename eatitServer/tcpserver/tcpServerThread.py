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
                print('TCP server :: client : ', data)

                datastr = str(data.decode())
                if datastr == 'eval':
                    self.geteval()
                # elif datastr == 'foods':
                #     self.getfoods()
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

        evaldata = self.getdata(64)
        if not evaldata:
            return
        print('evaldata: ', int(evaldata))
        print()

    # def getfoods(self):
    #     print()
    #     print('<get food>')
    #
    #     while(True):
    #         data = self.getdata(64)
    #         datastr = str(data.decode())
    #         print('initial: ', datastr)
    #         if datastr == 'finish':
    #             break
    #         else:
    #             templist = []
    #             templist.append(int(data))
    #             for i in range(1, 9):
    #                 data = self.getdata(64)
    #                 if str(data.decode()) == 'end':
    #                     break
    #                 templist.append(int(data))
    #             print(templist)
    #
    #     print('<get food finish>')
