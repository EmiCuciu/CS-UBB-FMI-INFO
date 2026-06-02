#ifndef LAB10_11_GUI_H
#define LAB10_11_GUI_H

#include "../Service/Service.h"
#include "../UI/UI.h"
#include "TableModel.h"
#include "ShoppingCartWindow.h"
#include "ShoppingCartDrawing.h"

#include <QApplication>
#include <QHeaderView>
#include <QPushButton>
#include <QHBoxLayout>
#include <QLineEdit>
#include <QLabel>
#include <QFormLayout>
#include <QListWidget>
#include <QMessageBox>
#include <QDialog>
#include <QTableWidget>
#include <QMainWindow>

class GUI : public QMainWindow {
private:
    Service &service;
    TableModel *model;

    QPushButton *add_btn;
    QPushButton *delete_btn;
    QPushButton *update_btn;
    QPushButton *search_btn;

    QPushButton *filter_year_btn;
    QPushButton *sort_title_btn;
    QPushButton *sort_year_btn;

    QPushButton *add_cart_btn;
    QPushButton *delete_cart_btn;
    QPushButton *populate_cart_btn;

    QPushButton *refresh_btn;
    QPushButton *undo_btn;
    QPushButton *cart_window_btn;
    QPushButton *cart_drawing_btn;

    std::unordered_map<std::string, QPushButton *> genre_buttons;

    QLineEdit *input_title;
    QLineEdit *input_author;
    QLineEdit *input_genre;
    QLineEdit *input_year;
    QSlider *input_count;

    QFormLayout *form_layout;
    QTableView *table_view;

    QWidget *main_widget;
    QHBoxLayout *main_layout;

    void initLayout();

    void connectSignals();

    void generateGenreButtons();

public:

    explicit GUI(Service &service, QWidget *parent = nullptr);
};

#endif //LAB10_11_GUI_H