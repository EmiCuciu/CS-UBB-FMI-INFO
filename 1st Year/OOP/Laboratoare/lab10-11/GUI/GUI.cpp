#include "GUI.h"

GUI::GUI(Service &service, QWidget *parent) : QMainWindow(parent), service{service} {
    initLayout();
    connectSignals();
    generateGenreButtons();
}

void GUI::initLayout() {
    main_widget = new QWidget(this);
    main_layout = new QHBoxLayout(main_widget);

    form_layout = new QFormLayout;

    input_title = new QLineEdit(this);
    input_author = new QLineEdit(this);
    input_genre = new QLineEdit(this);
    input_year = new QLineEdit(this);
    input_count = new QSlider(Qt::Horizontal, this);
    input_count->setRange(1, 10);

    form_layout->addRow(new QLabel("The title is:"), input_title);
    form_layout->addRow(new QLabel("The author is:"), input_author);
    form_layout->addRow(new QLabel("The genre is:"), input_genre);
    form_layout->addRow(new QLabel("The year is:"), input_year);
    form_layout->addRow(new QLabel("The count is:"), input_count);

    add_btn = new QPushButton{"Add"};
    delete_btn = new QPushButton{"Delete"};
    update_btn = new QPushButton{"Update"};

    search_btn = new QPushButton{"Search by title"};
    filter_year_btn = new QPushButton{"Filter by year"};

    sort_title_btn = new QPushButton{"Sort by title"};
    sort_year_btn = new QPushButton{"Sort by year"};

    add_cart_btn = new QPushButton{"Add to Cart"};
    delete_cart_btn = new QPushButton{"Delete Cart"};
    populate_cart_btn = new QPushButton{"Populate Cart"};

    undo_btn = new QPushButton{"Undo"};
    refresh_btn = new QPushButton{"Refresh"};
    cart_window_btn = new QPushButton{"Shopping Cart"};
    cart_drawing_btn = new QPushButton{"Cart Drawing"};

    auto *button_widget_crud = new QHBoxLayout;
    button_widget_crud->addWidget(add_btn);
    button_widget_crud->addWidget(delete_btn);
    button_widget_crud->addWidget(update_btn);

    auto *button_widget_filter = new QHBoxLayout;
    button_widget_filter->addWidget(search_btn);
    button_widget_filter->addWidget(filter_year_btn);

    auto *button_widget_cart = new QHBoxLayout;
    button_widget_cart->addWidget(add_cart_btn);
    button_widget_cart->addWidget(delete_cart_btn);
    button_widget_cart->addWidget(populate_cart_btn);

    auto *button_widget_sort = new QHBoxLayout;
    button_widget_sort->addWidget(sort_title_btn);
    button_widget_sort->addWidget(sort_year_btn);

    auto *button_widget_options = new QHBoxLayout;
    button_widget_options->addWidget(cart_window_btn);
    button_widget_options->addWidget(cart_drawing_btn);
    button_widget_options->addWidget(refresh_btn);
    button_widget_options->addWidget(undo_btn);

    form_layout->addRow(button_widget_crud);
    form_layout->addRow(button_widget_filter);
    form_layout->addRow(button_widget_sort);
    form_layout->addRow(button_widget_cart);

    table_view = new QTableView(this);
    model = new TableModel(service, this);
    table_view->setModel(model);
    table_view->setMinimumWidth(400);
    table_view->horizontalHeader()->setSectionResizeMode(QHeaderView::Stretch);

    auto *table_layout = new QVBoxLayout;
    table_layout->addWidget(table_view);
    table_layout->addLayout(button_widget_options);

    main_layout->addLayout(form_layout);
    main_layout->addLayout(table_layout);

    this->setCentralWidget(main_widget);
}

void GUI::connectSignals() {
    connect(table_view->selectionModel(), &QItemSelectionModel::selectionChanged, [this]() {
        const auto selected = table_view->selectionModel()->selectedRows();

        if (selected.isEmpty()) return;

        const auto index = selected.first();
        input_title->setText(model->data(model->index(index.row(), 0)).toString());
        input_author->setText(model->data(model->index(index.row(), 1)).toString());
        input_genre->setText(model->data(model->index(index.row(), 2)).toString());
        input_year->setText(model->data(model->index(index.row(), 3)).toString());
    });

    QObject::connect(add_btn, &QPushButton::clicked, [this]() {
        try {
            service.addCarteLibrarie(input_title->text().toStdString(), input_author->text().toStdString(),
                                     input_genre->text().toStdString(), input_year->text().toInt());
            model->refreshModel();

            QMessageBox::information(this, "Succes", "The element was added");
        } catch (const std::exception &error) {
            QMessageBox::warning(this, "Error", error.what());
        }
    });

    QObject::connect(delete_btn, &QPushButton::clicked, [this]() {
        const auto selected = table_view->selectionModel()->selectedRows();

        if (selected.isEmpty()) return;

        const auto index = selected.first();
        const auto name = model->data(model->index(index.row(), 0)).toString();

        try {
            service.deleteCarteLibrarie(name.toStdString());
            model->refreshModel();

            QMessageBox::information(this, "Succes", "The element was deleted");
        } catch (const std::exception &error) {
            QMessageBox::warning(this, "Error", error.what());
        }
    });

    QObject::connect(update_btn, &QPushButton::clicked, [this]() {
        try {
            service.updateCarteLibrarie(input_title->text().toStdString(), input_author->text().toStdString(),
                                        input_genre->text().toStdString(), input_year->text().toInt());
            model->refreshModel();

            QMessageBox::information(this, "Succes", "The element was updated");
        } catch (const std::exception &error) {
            QMessageBox::warning(this, "Error", error.what());
        }
    });

    QObject::connect(search_btn, &QPushButton::clicked, [this]() {
        try {
            auto books = service.cautaCarteLibrarie(input_title->text().toStdString());
            model->setRecords(books);
        } catch (const std::exception &error) {
            QMessageBox::warning(this, "Error", error.what());
        }
    });

    QObject::connect(filter_year_btn, &QPushButton::clicked, [this]() {
        try {
            auto filter_year = service.filtreazaCarti(input_year->text().toInt());
            model->setRecords(filter_year);
        } catch (const std::exception &error) {
            QMessageBox::warning(this, "Error", error.what());
        }
    });

    QObject::connect(sort_title_btn, &QPushButton::clicked, [this]() {
        auto sorted_title = service.sorteazaCarti(
                [&](const Carte &a, const Carte &b) {
                    return a.getTitlu() <= b.getTitlu();
                });

        model->setRecords(sorted_title);
    });

    QObject::connect(sort_year_btn, &QPushButton::clicked, [this]() {
        auto sorted_year = service.sorteazaCarti(
                [&](const Carte &a, const Carte &b) {
                    return a.getAn() <= b.getAn();
                });

        model->setRecords(sorted_year);
    });

    QObject::connect(add_cart_btn, &QPushButton::clicked, [this]() {
        const auto selected = table_view->selectionModel()->selectedRows();

        if (selected.isEmpty()) return;

        const auto index = selected.first();
        const auto Qname = model->data(model->index(index.row(), 0)).toString();

        try {
            service.addCarteCos(Qname.toStdString());

            QMessageBox::information(this, "Succes", "The element was added to cart");
        } catch (const std::exception &error) {
            QMessageBox::warning(this, "Error", error.what());
        }
    });

    QObject::connect(delete_cart_btn, &QPushButton::clicked, [this]() {
        try {
            service.deleteCos();

            QMessageBox::information(this, "Succes", "The element was added to cart");
        } catch (const std::exception &error) {
            QMessageBox::warning(this, "Error", error.what());
        }
    });

    QObject::connect(populate_cart_btn, &QPushButton::clicked, [this]() {
        int count = input_count->value();

        try {
            service.populateRandomCos(count);

            QMessageBox::information(this, "Succes", "The element was added to cart");
        } catch (const std::exception &error) {
            QMessageBox::warning(this, "Error", error.what());
        }
    });

    QObject::connect(cart_window_btn, &QPushButton::clicked, [this]() {
        auto *window = new ShoppingCartWindow(service);
        window->show();
    });

    QObject::connect(cart_drawing_btn, &QPushButton::clicked, [this]() {
        auto *window = new ShoppingCartDrawing(service);
        window->show();
    });

    QObject::connect(refresh_btn, &QPushButton::clicked, [this]() {
        model->refreshModel();
    });

    QObject::connect(undo_btn, &QPushButton::clicked, [this]() {
        try {
            service.undo();
            model->refreshModel();

            QMessageBox::information(this, "Succes", "The last operation was undone");
        } catch (const std::exception &error) {
            QMessageBox::warning(this, "Error", error.what());
        }
    });
}

void GUI::generateGenreButtons() {
    std::unordered_map<std::string, int> raport = service.getRaport();

    for (const auto &entry: raport) {
        const std::string &genre = entry.first;

        auto *genreButton = new QPushButton(QString::fromStdString(genre));
        form_layout->addRow(genreButton);

        QObject::connect(genreButton, &QPushButton::clicked, [&, genre]() {
            int count = service.getRaport()[genre];
            QMessageBox::information(this, "Report", QString("%1 books of genre %2").arg(count).arg(genre.c_str()));
        });

        genre_buttons[genre] = genreButton;
    }
}