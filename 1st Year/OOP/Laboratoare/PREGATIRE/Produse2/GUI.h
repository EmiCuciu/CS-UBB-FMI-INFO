#ifndef GUI_H
#define GUI_H

#include <QWidget>
#include <QTableView>
#include <QLineEdit>
#include <QDoubleSpinBox>
#include <QSlider>
#include <QPushButton>
#include <QLabel>
#include "MyTableModel.h"
#include "Service.h"

class GUI : public QWidget {
Q_OBJECT
private:
    Service &service;
    MyTableModel *tableModel;
    QTableView *tableView;
    QLineEdit *idLineEdit;
    QLineEdit *numeLineEdit;
    QLineEdit *tipLineEdit;
    QDoubleSpinBox *pretSpinBox;
    QSlider *pretSlider;
    QLabel *countLabel;
    QPushButton* add_btn = new QPushButton(this);

    void initGUI();
    void connectSignalsSlots();

public:
    explicit GUI(Service &service, QWidget *parent = nullptr);

public slots:
    void adaugaProdus();
    void updatePretFilter(int value);
};

#endif // GUI_H
