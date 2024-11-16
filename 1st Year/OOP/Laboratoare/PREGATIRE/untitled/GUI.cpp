//
// Created by Emi on 5/22/2024.
//

#include "GUI.h"

void GUI::initGUI() {
    this->setWindowTitle("GRESELI");
    this->resize(900,700);

    tabela->setHorizontalHeaderLabels(QStringList()<<"ID"<<"VersGresita"<<"VersCorecta"<<"Tip"<<"Severitate");
    tabela->horizontalHeader()->setSectionResizeMode(QHeaderView::Stretch);

    main_layout->addWidget(tabela);
    main_layout->addWidget(id_label);
    main_layout->addWidget(id_line);
    main_layout->addWidget(vg_label);
    main_layout->addWidget(vg_line);
    main_layout->addWidget(vc_label);
    main_layout->addWidget(vc_line);
    main_layout->addWidget(tip_label);
    main_layout->addWidget(tip_line);
    main_layout->addWidget(severity_label);
    main_layout->addWidget(severity_line);

    main_layout->addWidget(add_btn);
    main_layout->addWidget(sterge_btn);
    main_layout->addWidget(sort_vc_btn);
    main_layout->addWidget(sort_tip_btn);
    main_layout->addWidget(sort_severitate_btn);

    QObject::connect(add_btn,&QPushButton::clicked,[&](){addGUI();});
    QObject::connect(sterge_btn,&QPushButton::clicked,[&](){deleteGUI();});



    load();

}

void GUI::load() {
    tabela->setRowCount(0);

//    std::vector<Greseala>vector = service.getService();
    std::vector<Greseala>vector = service.sorteaza();

    for(auto item : vector){
        tabela->insertRow(tabela->rowCount());
        tabela->setItem(tabela->rowCount()-1,0,new QTableWidgetItem(QString::number(item.getID())));
        tabela->setItem(tabela->rowCount()-1,1,new QTableWidgetItem(QString::fromStdString(item.getVG())));
        tabela->setItem(tabela->rowCount()-1,2,new QTableWidgetItem(QString::fromStdString(item.getVC())));
        tabela->setItem(tabela->rowCount()-1,3,new QTableWidgetItem(QString::fromStdString(item.getTIP())));
        tabela->setItem(tabela->rowCount()-1,4,new QTableWidgetItem(QString::fromStdString(item.getSeverity())));
    }
}

void GUI::addGUI() {
    try {
        int id = id_line->text().toInt();
        std::string vg = vg_line->text().toStdString();
        std::string vc = vc_line->text().toStdString();
        std::string tip = tip_line->text().toStdString();
        std::string severity = severity_line->text().toStdString();

        service.addService(id, vg, vc, tip, severity);
        load();
    }
    catch (ServiceException &serviceException){
        QMessageBox::warning(this,"Service",serviceException.what());
    }
    catch (ValidariException &validariException){
        QMessageBox::warning(this,"Validare",validariException.what());
    }
}

void GUI::deleteGUI() {
    try {
        int id = id_line->text().toInt();
        service.deleteService(id);
    }
    catch (ServiceException &serviceException){
        QMessageBox::warning(this,"Service",serviceException.what());
    }
    catch (ValidariException &validariException){
        QMessageBox::warning(this,"Validare",validariException.what());
    }
}
