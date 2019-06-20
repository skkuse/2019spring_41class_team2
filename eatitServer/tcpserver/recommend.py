import numpy as np
import random

class Recommend:
    def __init__(self, uid):
        self.uid = uid
        self.items = []

    def run(self):

        filename = ''.join(['data/', str(self.uid), '_rcm.csv'])
        file = np.int_(np.loadtxt(filename, delimiter=',')).tolist()

        n = len(file)
        for i in range(10):
            index = random.randint(0, n)
            self.items.append(file[index])

