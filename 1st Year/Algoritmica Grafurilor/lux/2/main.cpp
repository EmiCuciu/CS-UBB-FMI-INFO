#include <queue>
#include <unordered_map>
#include <vector>
#include <functional>
#include "iostream"

struct HuffmanNode {
    char data;
    unsigned freq;
    HuffmanNode *left, *right;

    HuffmanNode(char data, unsigned freq) {
        left = right = nullptr;
        this->data = data;
        this->freq = freq;
    }
};

struct compare {
    bool operator()(HuffmanNode* l, HuffmanNode* r) {
        return (l->freq > r->freq);
    }
};

void printCodes(struct HuffmanNode* root, std::string str, std::unordered_map<char, std::string> &huffmanCode) {
    if (!root)
        return;

    if (root->data != '$')
        huffmanCode[root->data] = str;

    printCodes(root->left, str + "0", huffmanCode);
    printCodes(root->right, str + "1", huffmanCode);
}

void compressText(std::string text, std::unordered_map<char, std::string> &huffmanCode) {
    std::string str = "";
    for (auto i: text) {
        str += huffmanCode[i];
    }
    std::cout << "Compressed text is: " << str << std::endl;
}

void decompressText(struct HuffmanNode* root, std::string str) {
    std::string ans = "";
    struct HuffmanNode* curr = root;
    for (int i=0;i<str.size();i++) {
        if (str[i] == '0')
            curr = curr->left;
        else
            curr = curr->right;

        if (curr->left==NULL and curr->right==NULL) {
            ans += curr->data;
            curr = root;
        }
    }
    std::cout << "Decompressed text is: " << ans << std::endl;
}

void buildHuffmanTree(std::string text) {
    std::unordered_map<char, int> freq;
    for (auto x: text) {
        freq[x]++;
    }

    std::priority_queue<HuffmanNode*, std::vector<HuffmanNode*>, compare> h;

    for (auto i = freq.begin(); i != freq.end(); i++)
        h.push(new HuffmanNode(i->first, i->second));

    while (h.size() > 1) {
        HuffmanNode *x = h.top();
        h.pop();

        HuffmanNode *y = h.top();
        h.pop();

        HuffmanNode *sum = new HuffmanNode('$', x->freq + y->freq);
        sum->left = x;
        sum->right = y;
        h.push(sum);
    }

    HuffmanNode* root = h.top();

    std::unordered_map<char, std::string> huffmanCode;
    printCodes(root, "", huffmanCode);

    std::cout << "Huffman Codes are:\n";
    for (auto i = huffmanCode.begin(); i != huffmanCode.end(); i++)
        std::cout << i->first << " " << i->second << std::endl;

    compressText(text, huffmanCode);
    decompressText(root, "compressed text");
}

int main() {
    std::string text = "Treeaaassuureee";
    buildHuffmanTree(text);
    return 0;
}