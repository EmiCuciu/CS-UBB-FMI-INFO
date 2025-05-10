import numpy as np

class CustomANN:
    def __init__(self, input_size, hidden_sizes=[128, 64], output_size=3, lr=0.01, epochs=200):
        self.lr = lr
        self.epochs = epochs
        layer_sizes = [input_size] + hidden_sizes + [output_size]
        np.random.seed(42)
        self.weights = [
            np.random.randn(layer_sizes[i], layer_sizes[i + 1]) * np.sqrt(2 / layer_sizes[i])
            for i in range(len(layer_sizes) - 1)
        ]
        self.biases = [np.zeros((1, layer_sizes[i + 1])) for i in range(len(layer_sizes) - 1)]

    def relu(self, x):
        return np.maximum(0, x)

    def relu_deriv(self, x):
        return (x > 0).astype(float)

    def softmax(self, x):
        exp_x = np.exp(x - np.max(x, axis=1, keepdims=True))
        return exp_x / np.sum(exp_x, axis=1, keepdims=True)

    def forward(self, X):
        a = X
        self.zs, self.activations = [], [X]
        for W, b in zip(self.weights[:-1], self.biases[:-1]):
            z = a.dot(W) + b
            a = self.relu(z)
            self.zs.append(z)
            self.activations.append(a)
        z = a.dot(self.weights[-1]) + self.biases[-1]
        a = self.softmax(z)
        self.zs.append(z)
        self.activations.append(a)
        return a

    def backward(self, X, y):
        m = X.shape[0]
        delta = (self.activations[-1] - y)  # for softmax + cross-entropy
        for i in reversed(range(len(self.weights))):
            a_prev = self.activations[i]
            dw = a_prev.T.dot(delta) / m
            db = delta.sum(axis=0, keepdims=True) / m
            if i > 0:
                delta = delta.dot(self.weights[i].T) * self.relu_deriv(self.zs[i - 1])
            self.weights[i] -= self.lr * dw
            self.biases[i] -= self.lr * db

    def train(self, X, y):
        # Ensure y is one-hot encoded
        if y.ndim == 1 or y.shape[1] == 1:
            n_classes = np.max(y) + 1
            y_onehot = np.zeros((y.shape[0], n_classes))
            y_onehot[np.arange(y.shape[0]), y.flatten()] = 1
            y = y_onehot

        loss_history = []
        eps = 1e-8
        for epoch in range(self.epochs):
            out = self.forward(X)
            loss = -np.mean(np.sum(y * np.log(out + eps), axis=1))
            loss_history.append(loss)
            if epoch % 10 == 0:
                print(f"Epoch {epoch}, Loss: {loss:.4f}")
            self.backward(X, y)
        return loss_history

    def predict(self, X):
        prob = self.forward(X)
        return np.argmax(prob, axis=1)