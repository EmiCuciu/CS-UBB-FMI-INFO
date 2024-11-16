//
// Created by Emi on 5/22/2024.
//

#ifndef UNTITLED_GUI_H
#define UNTITLED_GUI_H

#include "Service.h"
#include "QWidget"
#include "QLayout"
#include "QTableWidget"
#include "QLabel"
#include "QLineEdit"
#include "QHeaderView"
#include "QPushButton"
#include "QMessageBox"
#include "QRadioButton"

class GUI : public QWidget{
private:
    Service &service;

    QVBoxLayout *main_layout = new QVBoxLayout(this);

    QHBoxLayout *rezultate_ly = new QHBoxLayout(this);

    QTableWidget *tabela = new QTableWidget(0,5, this);

    QLabel *id_label = new QLabel("ID",this);
    QLineEdit *id_line = new QLineEdit(this);
    QLabel *vg_label = new QLabel("Versiune Gresita",this);
    QLineEdit *vg_line = new QLineEdit(this);
    QLabel *vc_label = new QLabel("Versiune Corecta",this);
    QLineEdit *vc_line = new QLineEdit(this);
    QLabel *tip_label = new QLabel("TIP",this);
    QLineEdit *tip_line = new QLineEdit(this);
    QLabel *severity_label = new QLabel("Severitate",this);
    QLineEdit *severity_line = new QLineEdit(this);

    QPushButton *add_btn = new QPushButton("Adauga",this);
    QPushButton *sterge_btn = new QPushButton("Sterge",this);
    QRadioButton *sort_vc_btn = new QRadioButton("Sortare VC",this);
    QRadioButton *sort_tip_btn = new QRadioButton("Sortare TIP",this);
    QRadioButton *sort_severitate_btn = new QRadioButton("Sortare Severitate",this);



public:
    explicit GUI(Service &service) : service(service) {
        initGUI();
    }

    void initGUI();

    void addGUI();

    void deleteGUI();

    void load();
};

#endif //UNTITLED_GUI_H
