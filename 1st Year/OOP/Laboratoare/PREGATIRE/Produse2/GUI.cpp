#include "GUI.h"
#include <QVBoxLayout>
#include <QHBoxLayout>
#include <QMessageBox>

GUI::GUI(Service &service, QWidget *parent)
        : QWidget(parent), service(service) {
    setWindowTitle("Gestiune Produse");

    initGUI();
    connectSignalsSlots();
}

void GUI::initGUI() {
    QVBoxLayout *mainLayout = new QVBoxLayout(this);

    // Tabel pentru afișarea produselor
    tableView = new QTableView(this);
    tableModel = new MyTableModel(service, this);
    tableView->setModel(tableModel);

    // Formular pentru adăugare produs
    idLineEdit = new QLineEdit(this);
    idLineEdit->setPlaceholderText("Id");

    numeLineEdit = new QLineEdit(this);
    numeLineEdit->setPlaceholderText("Nume");

    tipLineEdit = new QLineEdit(this);
    tipLineEdit->setPlaceholderText("Tip");

    pretSpinBox = new QDoubleSpinBox(this);
    pretSpinBox->setRange(1.0, 100.0);
    pretSpinBox->setSingleStep(0.1);
    pretSpinBox->setDecimals(2);
    pretSpinBox->setPrefix("RON ");

    QHBoxLayout *inputLayout = new QHBoxLayout;
    inputLayout->addWidget(idLineEdit);
    inputLayout->addWidget(numeLineEdit);
    inputLayout->addWidget(tipLineEdit);
    inputLayout->addWidget(pretSpinBox);

    QPushButton *adaugaButton = new QPushButton("Adaugă produs", this);

    // Slider pentru filtrare după preț
    pretSlider = new QSlider(Qt::Horizontal, this);
    pretSlider->setRange(1, 100);
    pretSlider->setValue(50); // Default pentru filtrare
    pretSlider->setTickInterval(10);
    pretSlider->setTickPosition(QSlider::TicksBelow);

    countLabel = new QLabel(this);
    countLabel->setAlignment(Qt::AlignCenter);

    mainLayout->addWidget(tableView);
    mainLayout->addLayout(inputLayout);
    mainLayout->addWidget(adaugaButton);
    mainLayout->addWidget(pretSlider);
    mainLayout->addWidget(countLabel);

    updatePretFilter(50); // Inițializează filtrul

    setLayout(mainLayout);
}

void GUI::connectSignalsSlots() {
    QObject::connect(pretSlider, &QSlider::valueChanged, this, &GUI::updatePretFilter);
    QObject::connect(add_btn, &QPushButton::clicked, this, &GUI::adaugaProdus);
}

void GUI::adaugaProdus() {
    int id = idLineEdit->text().toInt();
    QString nume = numeLineEdit->text();
    QString tip = tipLineEdit->text();
    double pret = pretSpinBox->value();

    try {
        service.adaugaProdus(id, nume.toStdString(), tip.toStdString(), pret);
        tableModel->updateModel();

        // Actualizare număr de produse pentru tip
        int numarProduseTip = service.getNumarProduseTip(tip.toStdString());
        countLabel->setText(QString("Număr produse %1: %2").arg(tip).arg(numarProduseTip));
    } catch (const ServiceException &ex) {
        QMessageBox::warning(this, "Eroare", ex.what());
    }
}

void GUI::updatePretFilter(int value) {
    QPalette palette = tableView->palette();
    if (value == 50) {
        palette.setColor(QPalette::Highlight, Qt::red);
    } else {
        palette.setColor(QPalette::Highlight, Qt::white);
    }
    tableView->setPalette(palette);
    tableView->viewport()->update();
}
