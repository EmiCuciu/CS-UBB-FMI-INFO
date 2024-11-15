#include <iostream>
#include <fstream>
#include <vector>
#include <queue>
#include <climits>

using namespace std;

// Definim un tip de date pentru a reprezenta un nod și costul asociat
typedef pair<int, int> pii;

// Funcția Dijkstra care returnează distanțele minime de la nodul sursă la toate celelalte noduri
vector<int> dijkstra(int source, int V, vector<vector<pii>>& adj) {
    // Coada de priorități care va conține nodurile nevizitate
    priority_queue<pii, vector<pii>, greater<pii>> pq;
    // Vectorul de distanțe inițializat cu infinit
    vector<int> dist(V, INT_MAX);

    // Adăugăm nodul sursă în coada de priorități cu distanța 0
    pq.push(make_pair(0, source));
    dist[source] = 0;

    // Parcurgem coada de priorități
    while (!pq.empty()) {
        // Extragem nodul cu distanța minimă
        int u = pq.top().second;
        pq.pop();

        // Parcurgem vecinii nodului extras
        for (auto& i : adj[u]) {
            int v = i.first;
            int weight = i.second;

            // Dacă distanța până la vecin prin nodul extras este mai mică decât distanța curentă până la vecin
            if (dist[v] > dist[u] + weight) {
                // Actualizăm distanța și adăugăm vecinul în coadă
                dist[v] = dist[u] + weight;
                pq.push(make_pair(dist[v], v));
            }
        }
    }

    // Returnăm vectorul de distanțe
    return dist;
}

int main(int argc, char** argv) {
    // Deschidem fișierele de intrare și ieșire
    ifstream fin(argv[1]);
    ofstream fout(argv[2]);

    // Citim numărul de noduri, de muchii și nodul sursă
    int V, E, S;
    fin >> V >> E >> S;

    // Inițializăm lista de adiacență
    vector<vector<pii>> adj(V);

    // Citim muchiile
    for (int i = 0; i < E; i++) {
        int x, y, w;
        fin >> x >> y >> w;
        adj[x].push_back(make_pair(y, w));
    }

    // Aplicăm algoritmul Dijkstra
    vector<int> dist = dijkstra(S, V, adj);

    // Scriem rezultatele în fișierul de ieșire
    for (int i = 0; i < V; i++) {
        if (dist[i] != INT_MAX) {
            fout << dist[i] << " ";
        } else {
            fout << "INF ";
        }
    }

    // Închidem fișierele
    fin.close();
    fout.close();

    return 0;
}