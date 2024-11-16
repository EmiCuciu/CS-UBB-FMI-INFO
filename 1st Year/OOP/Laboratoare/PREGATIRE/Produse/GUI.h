// gui.h
#ifndef GUI_H
#define GUI_H

#include <QWidget>
#include <QVBoxLayout>
#include <QTableView>
#include <QLineEdit>
#include <QPushButton>
#include <QSlider>
#include <QLabel>
#include "service.h"
#include "mytablemodel.h"

class GUI : public QWidget {
    Q_OBJECT

private:
    Service& service;
    MyTableModel* model;
    QTableView* tableView;
    QLineEdit* idLineEdit;
    QLineEdit* numeLineEdit;
    QLineEdit* tipLineEdit;
    QLineEdit* pretLineEdit;
    QPushButton* adaugaButton;
    QSlider* slider;
    QLabel* sliderLabel;
    std::vector<QWidget*> tipWindows;

public:
    explicit GUI(Service& service, QWidget *parent = nullptr);

private slots:
            void adaugaProdus();
    void updateSlider(int value);
};

#endif // GUI_H
