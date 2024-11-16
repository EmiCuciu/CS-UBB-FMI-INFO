#include <iostream>
#include <vector>
#include "fstream"
#include "list"
#include "queue"
#include "stack"


using namespace std;


struct Graf {
    vector<vector<int>> matrice_adj;
    vector<list<int>> lista_adj;
};

Graf citire_graf(const string &filename) {
    Graf graf;

    ifstream in(filename);

    if (!in) {
        cerr << "Pula " << filename;
        return graf;
    }

    int nod, muchie;

    in >> nod >> muchie;

    graf.matrice_adj.resize(nod, vector<int>(nod, 0));
    graf.lista_adj.resize(nod);

    int u, v;

    while (in >> u >> v) {
        graf.matrice_adj[u][v] = 1;
        graf.matrice_adj[v][u] = 1;

        graf.lista_adj[u].push_back(v);
        graf.lista_adj[v].push_back(u);
    }

    in.close();
    return graf;
}

void print_mat_adj(vector<vector<int>> &mat_adj) {
    for (auto row: mat_adj) {
        for (auto val: row) {
            cout << val << " ";
        }
        cout << endl;
    }
}

void print_list_adj(vector<list<int>> &list_adj){
    for(int i=0;i<list_adj.size();i++){
        cout<<i<<" : ";
        for(auto item : list_adj[i]){
            cout<<item<<" ";
        }
        cout<<endl;
    }
}

void bfs(vector<list<int>>& mat_adj,int start){
    vector<bool> visited(mat_adj.size(), false);    // vector to keep track of visited nodes
    queue<int> q;   // queue to keep track of nodes to visit
    q.push(start);  // add the start node to the queue
    visited[start] = true;  // mark the start node as visited

    cout<<"BFS: ";
    while(!q.empty()){
        int nod = q.front();    // get the first element
        q.pop();            // remove the first element
        cout<<nod<<" ";    // print the element

        for(auto vecin : mat_adj[nod]){
            if(!visited[vecin]){
                visited[vecin] = true;
                q.push(vecin);
            }
        }
    }
    cout<<endl;
}

void dfsUtil(vector<list<int>>& adjlist,int node,vector<bool> &visited){
    visited[node] = true;
    cout<<node<<" ";

    for(int vecin : adjlist[node]){
        if(!visited[vecin]){
            dfsUtil(adjlist,vecin,visited);
        }
    }
}

void dfs(vector<list<int>>& adjlist,int start){
    vector<bool> visited(adjlist.size(), false);

    cout << "DFS: ";
    dfsUtil(adjlist,start,visited);
    cout<<endl;
}

int main() {
    Graf graf = citire_graf(R"(W:\Facultate S2\Algoritmica grafurilor\plm\1\graf.txt)");

    cout << "Matrice\n";
    print_mat_adj(graf.matrice_adj);

    cout << endl;

    cout << "Lista\n";
    print_list_adj(graf.lista_adj);

    cout << endl;

    bfs(graf.lista_adj,0);

    dfs(graf.lista_adj,0);

    return 0;
}
