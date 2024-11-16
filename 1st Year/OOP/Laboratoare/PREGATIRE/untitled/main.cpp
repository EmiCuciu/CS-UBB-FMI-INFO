#include <QApplication>
#include <QPushButton>

#include "GUI.h"

int main(int argc, char *argv[]) {
    QApplication a(argc, argv);

    Repository repository(R"(W:\Facultate S2\Programare orientata obiect\pregatire_simulare\untitled\greseli.txt)");
    Service service(repository);

    GUI gui(service);
    gui.show();

    return QApplication::exec();
}
