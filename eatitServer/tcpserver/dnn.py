import tensorflow as tf
import numpy as np

class DNN:
    def __init__(self):
        pass

    def run(self):

        learning_rate = 0.01
        training_epochs = 100
        batch_size = 5
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
        W = tf.Variable(tf.random_normal([7, 3]))
        b = tf.Variable(tf.random_normal([3]))
        hypothesis = tf.matmul(X, W) + b

        n, _ = xdata.shape

        # define cost/loss & optimizer
        cost = tf.reduce_mean(tf.nn.softmax_cross_entropy_with_logits(logits=hypothesis, labels=Y))
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
        print('Softmax Accuracy: ', sess.run(accuracy, feed_dict={X: xdata, Y: ydata}))

dnn = DNN()
dnn.run()
