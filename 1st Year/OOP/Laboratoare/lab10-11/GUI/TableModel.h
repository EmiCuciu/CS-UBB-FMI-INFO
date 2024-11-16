#ifndef LAB10_11_TABLEMODEL_H
#define LAB10_11_TABLEMODEL_H

#include <QAbstractTableModel>
#include "../Service/Service.h"

class TableModel : public QAbstractTableModel {
private:
    Service &service;
    std::vector<Carte> records;

public:
    explicit TableModel(Service &service, QObject *parent = nullptr);

    int rowCount(const QModelIndex &parent = QModelIndex()) const override;
    int columnCount(const QModelIndex &parent = QModelIndex()) const override;

    QVariant data(const QModelIndex &index, int role = Qt::DisplayRole) const override;
    QVariant headerData(int section, Qt::Orientation orientation, int role = Qt::DisplayRole) const override;

    void updateModel();
    void setRecords(std::vector<Carte> rec);
    void refreshModel();
};

#endif //LAB10_11_TABLEMODEL_H
