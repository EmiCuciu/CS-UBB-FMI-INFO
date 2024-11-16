#include "ShoppingCartWindow.h"

void ShoppingCartWindow::initLayout() {
    main_layout = new QVBoxLayout(this);
    list_widget = new QListWidget(this);

    form_layout = new QFormLayout;
    input_name = new QLineEdit(this);
    input_count = new QSlider(Qt::Horizontal, this);
    input_count->setRange(1, 10);

    form_layout->addRow(new QLabel("Enter the name:", this), input_name);
    form_layout->addRow(new QLabel("Enter the count:", this), input_count);

    btn_add = new QPushButton("Add", this);
    btn_delete = new QPushButton("Delete All", this);
    btn_populate = new QPushButton("Populate Random", this);
    btn_export_html = new QPushButton("Export HTML", this);

    form_layout->addRow(btn_add);
    form_layout->addRow(btn_delete);
    form_layout->addRow(btn_populate);
    form_layout->addRow(btn_export_html);

    main_layout->addWidget(list_widget);
    main_layout->addLayout(form_layout);
}

void ShoppingCartWindow::connectSignals() {
    service.addListener(this);

    connect(btn_add, &QPushButton::clicked, [this]() {
        std::string title = input_name->text().toStdString();

        try {
            service.addCarteCos(title);
            refreshList();
        } catch (const std::exception &error) {
            QMessageBox::warning(this, "Error", error.what());
        }
    });

    connect(btn_delete, &QPushButton::clicked, [this]() {
        try {
            service.deleteCos();
            refreshList();
        } catch (const std::exception &error) {
            QMessageBox::warning(this, "Error", error.what());
        }
    });

    connect(btn_populate, &QPushButton::clicked, [this]() {
        int count = input_count->value();

        try {
            service.populateRandomCos(count);
            refreshList();
        } catch (const std::exception &error) {
            QMessageBox::warning(this, "Error", error.what());
        }
    });

    connect(btn_export_html, &QPushButton::clicked, [this]() {
        try {
            service.exportCos(R"(W:\Facultate S2\Programare orientata obiect\teme\lab10-11\export.html)");
        } catch (const std::exception &error) {
            QMessageBox::warning(this, "Error", error.what());
        }
    });

}

void ShoppingCartWindow::refreshList() {
    list_widget->clear();
    for (const auto &item: service.getAllCos()) {
        QString q_string = QString::fromStdString(item.toString());
        list_widget->addItem(q_string);
    }
}
