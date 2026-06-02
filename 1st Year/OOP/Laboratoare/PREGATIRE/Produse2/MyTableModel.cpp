#include "MyTableModel.h"

MyTableModel::MyTableModel(Service &service, QObject *parent)
        : QAbstractTableModel(parent), service(service) {
    updateModel();
}

int MyTableModel::rowCount(const QModelIndex &parent) const {
    return produse.size();
}

int MyTableModel::columnCount(const QModelIndex &parent) const {
    return 4; // Id, Nume, Tip, Pret
}

QVariant MyTableModel::data(const QModelIndex &index, int role) const {
    if (!index.isValid())
        return QVariant();

    if (role == Qt::DisplayRole) {
        const Produs &produs = produse[index.row()];

        switch (index.column()) {
            case 0: // Id
                return produs.getId();
            case 1: // Nume
                return QString::fromStdString(produs.getNume());
            case 2: // Tip
                return QString::fromStdString(produs.getTip());
            case 3: // Pret
                return produs.getPret();
            default:
                return QVariant();
        }
    }

    return QVariant();
}

QVariant MyTableModel::headerData(int section, Qt::Orientation orientation, int role) const {
    if (role == Qt::DisplayRole) {
        if (orientation == Qt::Horizontal) {
            switch (section) {
                case 0:
                    return tr("Id");
                case 1:
                    return tr("Nume");
                case 2:
                    return tr("Tip");
                case 3:
                    return tr("Pret");
                default:
                    return QVariant();
            }
        }
    }

    return QVariant();
}

void MyTableModel::updateModel() {
    beginResetModel();
    produse = service.getProduse();
    endResetModel();
}
