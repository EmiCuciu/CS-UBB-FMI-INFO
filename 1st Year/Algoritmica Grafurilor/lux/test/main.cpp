#include <iostream>
#include <vector>
#include <map>
#include <set>


std::vector<int> prufer_code(std::map<int, std::set<int>> tree) {
    std::vector<int> prufer;
    int n = tree.size();

    for (int i = 0; i < n - 2; ++i) {
        int leaf = -1;
        for (const auto& node : tree) {
            if (node.second.size() == 1) {
                leaf = node.first;
                break;
            }
        }

        // Singurul vecin al frunzei
        int neighbor = *tree[leaf].begin();

        // Adăugăm vecinul la codul Prüfer
        prufer.push_back(neighbor);

        // Eliminăm frunza din arbore
        tree[neighbor].erase(leaf);
        tree.erase(leaf);
    }

    return prufer;
}

int main() {
    std::map<int, std::set<int>> tree1 = {
            {1, {2, 3, 4}},
            {2, {1}},
            {3, {1}},
            {4, {1, 5}},
            {5, {4}}
    };

    std::map<int, std::set<int>> tree2 = {
            {1, {2}},
            {2, {1, 3, 4}},
            {3, {2}},
            {4, {2}},
            {5, {2, 6}},
            {6, {5}}
    };

    std::vector<int> prufer1 = prufer_code(tree1);
    std::vector<int> prufer2 = prufer_code(tree2);

    std::cout << "Prufer code for tree1: ";
    for (int num : prufer1) {
        std::cout << num << " ";
    }
    std::cout << std::endl;

    std::cout << "Prufer code for tree2: ";
    for (int num : prufer2) {
        std::cout << num << " ";
    }
    std::cout << std::endl;

    return 0;
}
