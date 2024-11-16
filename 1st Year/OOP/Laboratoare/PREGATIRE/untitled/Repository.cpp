//
// Created by Emi on 5/22/2024.
//

#include <fstream>
#include <algorithm>
#include "Repository.h"
#include "sstream"

void Repository::addGreseala(Greseala &greseala) {
    items.push_back(greseala);
    writeToFile();
}


void Repository::loadFromFile() {
    std::ifstream in(file_name);

    std::string line;
    while (std::getline(in, line)) {
        std::stringstream stringstream(line);
        std::string token;
        std::vector<std::string> split;
        while (std::getline(stringstream, token, ',')) {
            split.push_back(token);
        }

        if(split.size() == 5){
            int id = stoi(split[0]);
            std::string vg = split[1];
            std::string vc = split[2];
            std::string tip = split[3];
            std::string sever = split[4];

            Greseala greseala(id,vg,vc,tip,sever);
            addGreseala(greseala);
        }

    }
    in.close();
}

void Repository::writeToFile() {
    std::ofstream out(file_name);

    for (auto it: items) {
        out<<it.getID()<<","<<it.getVG()<<","<<it.getVC()<<","<<it.getTIP()<<","<<it.getSeverity()<<"\n";
    }
    out.close();
}


size_t Repository::getSize() {
    return items.size();
}

std::vector<Greseala> Repository::getGreseli() {
    return items;
}

void Repository::deleteGreseala(std::vector<Greseala>::iterator &pos) {
    if(pos != items.end()){
        items.erase(pos);
        writeToFile();
    }
}

std::vector<Greseala>::iterator Repository::findGreseala(int id) {
    return std::find_if(items.begin(),items.end(),[&](Greseala &greseala){
        return greseala.getID() == id;
    });
}

bool Repository::validatePosition(std::vector<Greseala>::iterator &pos) {
        return pos != items.end();
}
