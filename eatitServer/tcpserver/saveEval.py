import numpy as np
import os.path

class SaveEval:
    def __init__(self, uid, fid, y):
        self.uid = uid
        self.fid = fid
        self.y = y

    def run(self):
        # print('uid: ', self.uid, ', fid: ', self.fid, ', y: ', self.y)

        food = np.loadtxt('data/food.csv', delimiter=',')
        # print(food)

        # find appropriate row data for fid
        row = np.zeros((1, 8))
        for i in range(0, food.shape[0]):
            if self.fid == food[i][0]:
                row[0] = food[i]
                break
        # print(row)
        y = np.zeros((1, 3))
        y[0] = self.y
        # print('y: ', y)

        filename = ''.join(['data/', str(self.uid), '.csv'])
        filenameY = ''.join(['data/', str(self.uid), '_y.csv'])

        if os.path.isfile(filename):
            print('exists')
            file = np.loadtxt(filename, delimiter=',', ndmin=2)
            fileY = np.loadtxt(filenameY, delimiter=',', ndmin=2)

            # 해당 fid가 이미 존재하는지 검사. 없을시 그냥 append/있을시 y의 row만 교체
            for i in range(0, file.shape[0]):
                if file[i][0] == row[0][0]:
                    fileY[i] = y
                    break
                if i == file.shape[0] - 1:
                    file = np.append(file, row, axis=0)
                    fileY = np.append(fileY, y, axis=0)
            np.savetxt(filename, file, fmt='%10.5f', delimiter=',')
            np.savetxt(filenameY, fileY, fmt='%10.5f', delimiter=',')
        else:
            print('not exists')
            np.savetxt(filename, row, fmt='%10.5f', delimiter=',')
            np.savetxt(filenameY, y, fmt='%10.5f', delimiter=',')
        return

