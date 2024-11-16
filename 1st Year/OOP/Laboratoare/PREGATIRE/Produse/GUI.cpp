#include "gui.h"
#include <QMessageBox>
#include <QHBoxLayout>

GUI::GUI(Service& service, QWidget *parent)
        : QWidget(parent), service(service) {
    QVBoxLayout* mainLayout = new QVBoxLayout(this);

    // Tabelul cu produse
    model = new MyTableModel(service, this);
    tableView = new QTableView(this);
    tableView->setModel(model);
    mainLayout->addWidget(tableView);

    // Formular adaugare produs
    QHBoxLayout* formLayout = new QHBoxLayout();
    mainLayout->addLayout(formLayout);

    idLineEdit = new QLineEdit(this);
    idLineEdit->setPlaceholderText("ID");
    formLayout->addWidget(idLineEdit);

    numeLineEdit = new QLineEdit(this);
    numeLineEdit->setPlaceholderText("Nume");
    formLayout->addWidget(numeLineEdit);

    tipLineEdit = new QLineEdit(this);
    tipLineEdit->setPlaceholderText("Tip");
    formLayout->addWidget(tipLineEdit);

    pretLineEdit = new QLineEdit(this);
    pretLineEdit->setPlaceholderText("Preț");
    formLayout->addWidget(pretLineEdit);

    adaugaButton = new QPushButton("Adaugă", this);
    connect(adaugaButton, &QPushButton::clicked, this, &GUI::adaugaProdus);
    formLayout->addWidget(adaugaButton);

    // Slider pentru filtrare
    slider = new QSlider(Qt::Horizontal, this);
    slider->setRange(0, 100);
    connect(slider, &QSlider::valueChanged, this, &GUI::updateSlider);
    mainLayout->addWidget(slider);

    sliderLabel = new QLabel(this);
    mainLayout->addWidget(sliderLabel);

    // Initializare slider
    updateSlider(slider->value());

    // Ferestre pentru tipuri de produse
    auto tip_count = service.NumarTip();
    for (const auto& pair : tip_count) {
        QWidget* tipWindow = new QWidget(this);
        tipWindows.push_back(tipWindow);

        QVBoxLayout* tipLayout = new QVBoxLayout(tipWindow);
        QLabel* titleLabel = new QLabel(QString::fromStdString(pair.first), tipWindow);
        tipLayout->addWidget(titleLabel);

        QLabel* countLabel = new QLabel(QString::number(pair.second), tipWindow);
        tipLayout->addWidget(countLabel);

        tipWindow->setLayout(tipLayout);
        tipWindow->setWindowTitle(QString::fromStdString(pair.first));
        tipWindow->show();
    }

    setLayout(mainLayout);
    setWindowTitle("Gestiune Produse");
}

void GUI::adaugaProdus() {
    try {
        int id = idLineEdit->text().toInt();
        std::string nume = numeLineEdit->text().toStdString();
        std::string tip = tipLineEdit->text().toStdString();
        double pret = pretLineEdit->text().toDouble();

        service.addProduct(id, nume, tip, pret);

        // Actualizare număr produse pe ferestre tip
        auto tip_count = service.NumarTip();
        for (size_t i = 0; i < tipWindows.size(); ++i) {
            QLabel* countLabel = tipWindows[i]->findChild<QLabel*>();
            if (countLabel) {
                std::string tip = tip_count.begin() + i;
                countLabel->setText(QString::number(tip_count[tip]));
            }
        }

        // Actualizare tabel
        model->beginResetModel();
        model->endResetModel();

        QMessageBox::information(this, "Adăugare produs", "Produs adăugat cu succes!");
    } catch (const ValidationException& ex) {
        QMessageBox::warning(this, "Validare produs", QString::fromStdString(ex.what()));
    } catch (const ServiceException& ex) {
        QMessageBox::warning(this, "Adăugare produs", QString::fromStdString(ex.what()));
    } catch (...) {
        QMessageBox::critical(this, "Eroare", "Eroare la adăugare produs!");
    }
}

void GUI::updateSlider(int value) {
    sliderLabel->setText("Filtru preț: " + QString::number(value));

    // Filtrare produse în funcție de preț
    auto products = service.getProducts();
    for (const auto& product : products) {
        QModelIndex index = model->index(std::distance(products.data(), &product), 3);
        if (product.getPret() <= value) {
            tableView->setRowHidden(index.row(), false);
        } else {
            tableView->setRowHidden(index.row(), true);
        }
    }
}
