//
// Created by Emi on 4/15/2024.
//
#include "Service/Service.h"
#include <fstream>

void Service::exportCos(const std::string &filename) {
    std::ofstream out(filename);
    out << "<!DOCTYPE html>\n"
           "<html>\n"
           "    <head>\n"
           "        <title>Cos</title>\n"
           "    </head>\n"
           "<body>\n"
           "    <h1>Cos</h1>\n"
           "    <ul>\n";

    for (const auto &carte: getAllCos()) {
        out << "        <li>" << "Titlu: " << carte.getTitlu() << ", " << "Autor: " << carte.getAutor() << ", "
            << "Gen: " << carte.getGen() << ", " << "An: " << carte.getAn() << "</li>\n";
    }

    out << "    </ul>\n"
           "    </body>\n"
           "</html>\n";
    out.close();
}

