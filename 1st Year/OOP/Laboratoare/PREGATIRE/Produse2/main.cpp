#include <QApplication>
#include "Service.h"
#include "GUI.h"

int main(int argc, char *argv[]) {
    QApplication a(argc, argv);

    Service service("produse.txt");
    GUI gui(service);
    gui.show();

    return a.exec();
}
