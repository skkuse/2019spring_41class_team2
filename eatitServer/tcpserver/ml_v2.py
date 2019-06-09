import tensorflow as tf
import numpy as np

# Machine Learning version 1: DNN
# Layer 갯수: 5 (3 hidden layers)
# Activation function: ReLU
# Initialization: Xavier
# Optimizer: Adam
# Dropout: 삭제
class DNN:
    def __init__(self):
        pass

    def run(self):

        learning_rate = 0.01
        training_epochs = 100
        batch_size = 5
        keep_prob = tf.placeholder(tf.float32)

        xdata = np.array([[0, 2, 0, 118, 0, 0, 1], [3, 1, 2, 64, 2, 3, 2],
                      [3, 1, 2, 57, 2, 3, 2], [0, 4, 0, 162, 1, 3, 2],
                      [3, 1, 0, 500, 2, 1, 2], [2, 0, 0, 55, 2, 3, 2],
                      [2, 0, 0, 55, 2, 3, 2], [1, 4, 0, 50, 2, 6, 1],
                      [3, 3, 0, 250, 0, 3, 1], [1, 2, 1, 160, 2, 1, 0]])
        ydata = np.array([[1, 0, 0], [1, 0, 0], [1, 0, 0], [0, 1, 0],
                      [1, 0, 0], [1, 0, 0], [0, 0, 1], [1, 0, 0],
                      [0, 1, 0], [0, 1, 0]])

        X = tf.placeholder(tf.float32, [None, 7])
        Y = tf.placeholder(tf.float32, [None, 3])

        W1 = tf.get_variable("W1", shape=[7, 7], initializer=tf.contrib.layers.xavier_initializer())
        # W1 = tf.Variable(tf.random_normal([7, 7]))
        b1 = tf.Variable(tf.random_normal([7]))
        L1 = tf.nn.relu(tf.matmul(X, W1) + b1)
        # L1 = tf.nn.dropout(L1, keep_prob=keep_prob)

        W2 = tf.get_variable("W2", shape=[7, 6], initializer=tf.contrib.layers.xavier_initializer())
        # W2 = tf.Variable(tf.random_normal([7, 6]))
        b2 = tf.Variable(tf.random_normal([6]))
        L2 = tf.nn.relu(tf.matmul(L1, W2) + b2)
        # L2 = tf.nn.dropout(L2, keep_prob=keep_prob)

        W3 = tf.get_variable("W3", shape=[6, 5], initializer=tf.contrib.layers.xavier_initializer())
        # W3 = tf.Variable(tf.random_normal([6, 5]))
        b3 = tf.Variable(tf.random_normal([5]))
        L3 = tf.nn.relu(tf.matmul(L2, W3) + b3)
        # L3 = tf.nn.dropout(L3, keep_prob=keep_prob)

        W4 = tf.get_variable("W4", shape=[5, 4], initializer=tf.contrib.layers.xavier_initializer())
        # W4 = tf.Variable(tf.random_normal([5, 4]))
        b4 = tf.Variable(tf.random_normal([4]))
        L4 = tf.nn.relu(tf.matmul(L3, W4) + b4)
        # L4= tf.nn.dropout(L4, keep_prob=keep_prob)

        W5 = tf.get_variable("W5", shape=[4, 3], initializer=tf.contrib.layers.xavier_initializer())
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
                end = (i+1) * batch_size
                if end >= n:
                    end = n
                batch_xs = xdata[start:end]
                batch_ys = ydata[start:end]

                feed_dict = {X: batch_xs, Y: batch_ys}
                c, _ = sess.run([cost, optimizer], feed_dict=feed_dict)
                avg_cost += c / total_batch

            print('Epoch: ', '%04d' % (epoch+1), 'cost=', '{:.9f}'.format(avg_cost))

        print('Learning Finished!')

        # Test model and check accuracy
        correct_prediction = tf.equal(tf.argmax(hypothesis, 1), tf.argmax(Y, 1))
        accuracy = tf.reduce_mean(tf.cast(correct_prediction, tf.float32))
        print('DNN1 Accuracy: ', sess.run(accuracy, feed_dict={X: xdata, Y: ydata}))

dnn = DNN()
dnn.run()
