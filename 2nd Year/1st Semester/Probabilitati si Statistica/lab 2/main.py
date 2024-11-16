import random
import matplotlib.pyplot as plt
import random


# def simulate_event(N):
#     successes = 0
#     for _ in range(N):
#         draw = random.choice([0, 1])  # Simulate a simple 0/1 event (like a coin flip)
#         if draw == 1:  # Consider 1 as a "success"
#             successes += 1
#     probability = successes / N
#     return probability
#
#
# N = 1000  # You can choose the number of simulations
# print(f"Estimated probability: {simulate_event(N)}")
#
#
# def plot_simulation_results(N_values):
#     probabilities = [simulate_event(N) for N in N_values]
#
#     plt.plot(N_values, probabilities, marker='o')
#     plt.xlabel('Number of Simulations (N)')
#     plt.ylabel('Estimated Probability')
#     plt.title('Probability Estimate as Function of N')
#     plt.grid(True)
#     plt.show()
#
#
# N_values = range(10, 1001, 10)  # You can increase the step size if needed
# plot_simulation_results(N_values)


def geometric_probability(N):
    inside_circle = 0
    total_points = N

    for _ in range(total_points):
        x, y = random.uniform(-1, 1), random.uniform(-1, 1)  # Random points in a square [-1, 1] x [-1, 1]
        if x ** 2 + y ** 2 <= 1:  # Check if the point is inside the unit circle
            inside_circle += 1

    probability = inside_circle / total_points
    return probability


N = 10000  # Number of random points
print(f"Geometric probability estimate: {geometric_probability(N)}")


def plot_geometric_points(N):
    x_inside, y_inside = [], []
    x_outside, y_outside = [], []

    for _ in range(N):
        x, y = random.uniform(-1, 1), random.uniform(-1, 1)
        if x ** 2 + y ** 2 <= 1:
            x_inside.append(x)
            y_inside.append(y)
        else:
            x_outside.append(x)
            y_outside.append(y)

    plt.scatter(x_inside, y_inside, color='cyan', label='Inside Circle', s=1)
    plt.scatter(x_outside, y_outside, color='orange', label='Outside Circle', s=1)
    plt.xlabel('x')
    plt.ylabel('y')
    plt.title('Random Points Inside and Outside a Circle')
    plt.gca().set_aspect('equal', adjustable='box')
    plt.show()


plot_geometric_points(50000)
