#include <QApplication>
#include "service.h"
#include "gui.h"

int main(int argc, char *argv[]) {
    QApplication a(argc, argv);

    // Creare service și GUI
    Repository repo("produse.txt"); // Fișierul cu produse inițiale
    Service service(repo);
    GUI gui(service);
    gui.show();

    return a.exec();
}
