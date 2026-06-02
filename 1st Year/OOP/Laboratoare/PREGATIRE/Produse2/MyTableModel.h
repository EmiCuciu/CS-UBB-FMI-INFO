#ifndef MYTABLEMODEL_H
#define MYTABLEMODEL_H

#include <QAbstractTableModel>
#include "Service.h"

class MyTableModel : public QAbstractTableModel {
Q_OBJECT
private:
    std::vector<Produs> produse;
    Service &service;

public:
    explicit MyTableModel(Service &service, QObject *parent = nullptr);

    int rowCount(const QModelIndex &parent = QModelIndex()) const override;
    int columnCount(const QModelIndex &parent = QModelIndex()) const override;
    QVariant data(const QModelIndex &index, int role = Qt::DisplayRole) const override;
    QVariant headerData(int section, Qt::Orientation orientation, int role) const override;

    void updateModel();
};

#endif // MYTABLEMODEL_H
