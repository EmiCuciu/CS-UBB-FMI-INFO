#include <QApplication>
#include <QPushButton>
#include "UI/UI.h"
#include "Teste/Teste.h"

#include "GUI/GUI.h"

int main(int argc, char *argv[]) {

    Teste teste;
    teste.runAllTests();

    Librarie lib;
    Cos cos;

    Service srv(lib, cos);
    srv.addpredefinite();
    //UI ui(srv);
    //ui.run();

    QApplication a(argc, argv);

    GUI gui(srv);
    gui.setWindowTitle("Library");
    gui.show();

    return QApplication::exec();
}
