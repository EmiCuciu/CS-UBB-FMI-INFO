#include "UI/UI.h"
#include "Teste/Teste.h"

int main() {
    Teste test;
    test.runTests();

    Repository repo;
    Service service(repo);
    UI ui(service);

    ui.run();
    return 0;
}
