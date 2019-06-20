import tensorflow as tf
import numpy as np

# 하는일
# userid를 input으로 받음
# user에 맞는 data file들을 읽어 전체 food에 대한 취향 예측 파일 생성 (uid_rcm.csv)

# Machine Learning version 1: DNN
# Layer 갯수: 5 (3 hidden layers)
# Activation function: ReLU
# Initialization: He (normal)
# Optimizer: Adam
# Dropout: 삭제
class DNN:
    def __init__(self, uid):
        self.uid = uid
        self.accuracy = 0.
        self.y = None

    def run(self):

        learning_rate = 0.01
        training_epochs = 100
        batch_size = 10
        keep_prob = tf.placeholder(tf.float32)

        filenameX = ''.join(['data/', str(self.uid), '.csv'])
        filenameY = ''.join(['data/', str(self.uid), '_y.csv'])

        # xdata = np.array([[0, 2, 0, 118, 0, 0, 1], [3, 1, 2, 64, 2, 3, 2],
        #         #               [3, 1, 2, 57, 2, 3, 2], [0, 4, 0, 162, 1, 3, 2],
        #         #               [3, 1, 0, 500, 2, 1, 2], [2, 0, 0, 55, 2, 3, 2],
        #         #               [2, 0, 0, 55, 2, 3, 2], [1, 4, 0, 50, 2, 6, 1],
        #         #               [3, 3, 0, 250, 0, 3, 1], [1, 2, 1, 160, 2, 1, 0]])
        #         # ydata = np.array([[1, 0, 0], [1, 0, 0], [1, 0, 0], [0, 1, 0],
        #         #               [1, 0, 0], [1, 0, 0], [0, 0, 1], [1, 0, 0],
        #         #               [0, 1, 0], [0, 1, 0]])

        xdata = np.loadtxt(filenameX, delimiter=',', ndmin=2)[..., 1:]
        ydata = np.loadtxt(filenameY, delimiter=',', ndmin=2)

        # print('xdata')
        # print(xdata)
        # print('ydata')
        # print(ydata)

        X = tf.placeholder(tf.float32, [None, 7])
        Y = tf.placeholder(tf.float32, [None, 3])

        W1 = tf.get_variable("W1", shape=[7, 7], initializer=tf.initializers.he_normal())
        # W1 = tf.get_variable("W1", shape=[7, 7], initializer=tf.contrib.layers.xavier_initializer())
        # W1 = tf.Variable(tf.random_normal([7, 7]))
        b1 = tf.Variable(tf.random_normal([7]))
        L1 = tf.nn.relu(tf.matmul(X, W1) + b1)
        # L1 = tf.nn.dropout(L1, keep_prob=keep_prob)

        W2 = tf.get_variable("W2", shape=[7, 6], initializer=tf.initializers.he_normal())
        # W2 = tf.Variable(tf.random_normal([7, 6]))
        b2 = tf.Variable(tf.random_normal([6]))
        L2 = tf.nn.relu(tf.matmul(L1, W2) + b2)
        # L2 = tf.nn.dropout(L2, keep_prob=keep_prob)

        W3 = tf.get_variable("W3", shape=[6, 5], initializer=tf.initializers.he_normal())
        # W3 = tf.Variable(tf.random_normal([6, 5]))
        b3 = tf.Variable(tf.random_normal([5]))
        L3 = tf.nn.relu(tf.matmul(L2, W3) + b3)
        # L3 = tf.nn.dropout(L3, keep_prob=keep_prob)

        W4 = tf.get_variable("W4", shape=[5, 4], initializer=tf.initializers.he_normal())
        # W4 = tf.Variable(tf.random_normal([5, 4]))
        b4 = tf.Variable(tf.random_normal([4]))
        L4 = tf.nn.relu(tf.matmul(L3, W4) + b4)
        # L4= tf.nn.dropout(L4, keep_prob=keep_prob)

        W5 = tf.get_variable("W5", shape=[4, 3], initializer=tf.initializers.he_normal())
        # W5 = tf.Variable(tf.random_normal([4, 3]))
        b5 = tf.Variable(tf.random_normal([3]))
        hypothesis = tf.matmul(L4, W5) + b5
        # hypothesis = tf.nn.dropout(hypothesis, keep_prob=keep_prob)

        n, _ = xdata.shape

        # define cost/loss & optimizer
        l2reg = 0.01 * (tf.reduce_sum(tf.square(W1)) + tf.reduce_sum(tf.square(W2)) + tf.reduce_sum(tf.square(W3)) + tf.reduce_sum(tf.square(W4)) + tf.reduce_sum(tf.square(W5)))
        cost = tf.reduce_mean(tf.nn.softmax_cross_entropy_with_logits(logits=hypothesis, labels=Y))
        cost += l2reg
        optimizer = tf.train.AdamOptimizer(learning_rate=learning_rate).minimize(cost)

        # initialize
        sess = tf.Session()
        sess.run(tf.global_variables_initializer())

        # train my model
        for epoch in range(training_epochs):
            avg_cost = 0
            total_batch = int(n / batch_size)

            for i in range(total_batch):
                start = i * batch_size
                end = (i + 1) * batch_size
                if end >= n:
                    end = n
                batch_xs = xdata[start:end]
                batch_ys = ydata[start:end]

                feed_dict = {X: batch_xs, Y: batch_ys}
                c, _ = sess.run([cost, optimizer], feed_dict=feed_dict)
                avg_cost += c / total_batch

            # print('Epoch: ', '%04d' % (epoch+1), 'cost=', '{:.9f}'.format(avg_cost))
        # print('Learning Finished!')

        # Test model and check accuracy
        correct_prediction = tf.equal(tf.argmax(hypothesis, 1), tf.argmax(Y, 1))
        accuracy = tf.reduce_mean(tf.cast(correct_prediction, tf.float32))
        acc = sess.run(accuracy, feed_dict={X: xdata, Y: ydata})
        print('DNN1 Accuracy: ', acc)

        # print()
        argmax = tf.argmax(hypothesis, 1)
        result = sess.run(argmax, feed_dict={X: xdata, Y: ydata})
        # print(result)

        # print()
        argmax = tf.argmax(hypothesis, 1)
        xdata_whole = np.loadtxt('data/food.csv', delimiter=',')[..., 1:]
        result = sess.run(argmax, feed_dict={X: xdata_whole, Y: np.zeros((xdata_whole.shape[0], 3))})
        # print(result)

        self.accuracy = acc
        self.y = result

    def train(self):
        while self.accuracy < 0.5:
            tf.reset_default_graph()
            self.run()
        self.save()

    # 'uid_rcm.csv'에 good일거라고(0) 예측되는 fid들 저장
    def save(self):
        good = []
        for i in range(0, self.y.shape[0]):
            if self.y[i] == 0:
                good.append(i+1)

        # print('good 개수: ', len(good))
        filename = ''.join(['data/', str(self.uid), '_rcm.csv'])
        np.savetxt(filename, good, fmt='%i', delimiter=',')


# dnn = DNN(86375292)
# y = None
# while dnn.accuracy < 0.5:
#     tf.reset_default_graph()
#     dnn.run()
# print()
# print('Accuracy: ', dnn.accuracy)
# print(dnn.y)
# dnn.save()
