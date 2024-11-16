/**
 * Algoritmul lui Johnson este o modalitate de a găsi cele mai scurte căi între toate perechile de vârfuri
 * într-un grafic direcționat ponderat cu muchii . Permite ca unele dintre greutățile de margine să fie numere negative ,
 * dar nu pot exista cicluri de greutate negativă . Funcționează folosind algoritmul Bellman-Ford pentru a calcula o
 * transformare a graficului de intrare care elimină toate greutățile negative, permițând algoritmului lui Dijkstra să fie
 * utilizat pe graficul transformat.  Este numit după Donald B. Johnson , care a publicat prima tehnică în 1977.
 *
 * Algoritmul lui Johnson este util pentru a găsi cele mai scurte căi între toate perechile de vârfuri într-un grafic
 *
 * Wikipedia
 */

#include <iostream>
#include <vector>
#include <fstream>
#include <queue>
#include <limits>

#define INF std::numeric_limits<int>::max()

// Structura pentru a stoca o muchie a grafului
struct Edge {
    int from, to, weight;
};

// Implementarea algoritmului Bellman-Ford pentru a detecta ciclurile negative
std::vector<int> BellmanFord(int V, int source, std::vector<Edge>& edges) {
    std::vector<int> distance(V, INF);
    distance[source] = 0;

    // Relaxarea muchiilor
    for (int i = 0; i < V - 1; ++i) {
        for (const auto& edge : edges) {
            if (distance[edge.from] != INF && distance[edge.from] + edge.weight < distance[edge.to]) {
                distance[edge.to] = distance[edge.from] + edge.weight;
            }
        }
    }

    // Verificarea ciclurilor negative
    for (const auto& edge : edges) {
        if (distance[edge.from] != INF && distance[edge.from] + edge.weight < distance[edge.to]) {
            throw std::runtime_error("Ciclu negativ detectat");
        }
    }

    return distance;
}

// Implementarea algoritmului Dijkstra pentru a găsi cele mai scurte căi
std::vector<int> Dijkstra(int V, int source, std::vector<std::vector<std::pair<int, int>>>& adj) {
    std::vector<int> distance(V, INF);
    distance[source] = 0;

    std::priority_queue<std::pair<int, int>, std::vector<std::pair<int, int>>, std::greater<>> pq;
    pq.push({0, source});

    while (!pq.empty()) {
        int u = pq.top().second;
        pq.pop();

        for (const auto& edge : adj[u]) {
            int v = edge.first;
            int weight = edge.second;

            if (distance[u] + weight < distance[v]) {
                distance[v] = distance[u] + weight;
                pq.push({distance[v], v});
            }
        }
    }

    return distance;
}

int main(int argc, char** argv) {
    std::ifstream fin(argv[1]);
    std::ofstream fout(argv[2]);

    int V, E;
    fin >> V >> E;

    std::vector<Edge> edges(E + V);
    for (int i = 0; i < E; ++i) {
        fin >> edges[i].from >> edges[i].to >> edges[i].weight;
    }

    // Adăugarea unui nod suplimentar care este conectat la toate celelalte noduri
    for (int i = 0; i < V; ++i) {
        edges[E + i] = {V, i, 0};
    }

    std::vector<int> h;
    try {
        // Detectarea ciclurilor negative cu ajutorul algoritmului Bellman-Ford
        h = BellmanFord(V + 1, V, edges);
    } catch (const std::runtime_error& e) {
        fout << -1;
        return 0;
    }

    // Crearea grafului reponderat
    std::vector<std::vector<std::pair<int, int>>> adj(V);
    for (int i = 0; i < E; ++i) {
        int u = edges[i].from;
        int v = edges[i].to;
        int weight = edges[i].weight + h[u] - h[v];
        adj[u].push_back({v, weight});
    }

    // Calcularea celor mai scurte căi cu ajutorul algoritmului Dijkstra
    for (int i = 0; i < V; ++i) {
        std::vector<int> distance = Dijkstra(V, i, adj);
        for (int j = 0; j < V; ++j) {
            if (distance[j] == INF) {
                fout << "INF ";
            } else {
                fout << distance[j] + h[j] - h[i] << " ";
            }
        }
        fout << "\n";
    }

    return 0;
}