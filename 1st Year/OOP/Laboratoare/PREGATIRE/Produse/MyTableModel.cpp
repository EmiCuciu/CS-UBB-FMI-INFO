#include "mytablemodel.h"

MyTableModel::MyTableModel(Service& service, QObject *parent)
        : QAbstractTableModel(parent), service(service) {}

int MyTableModel::rowCount(const QModelIndex &parent) const {
    Q_UNUSED(parent);
    return service.getProducts().size();
}

int MyTableModel::columnCount(const QModelIndex &parent) const {
    Q_UNUSED(parent);
    return 5; // id, nume, tip, pret, număr vocale
}

QVariant MyTableModel::data(const QModelIndex &index, int role) const {
    if (!index.isValid()) {
        return QVariant();
    }

    if (index.row() >= service.getProducts().size() || index.row() < 0) {
        return QVariant();
    }

    if (role == Qt::DisplayRole) {
        const auto& product = service.getProducts().at(index.row());

        switch (index.column()) {
            case 0:
                return product.getId();
            case 1:
                return QString::fromStdString(product.getNume());
            case 2:
                return QString::fromStdString(product.getTip());
            case 3:
                return product.getPret();
            case 4:
                return product.numarVocale(); // Numărul de vocale din numele produsului
            default:
                return QVariant();
        }
    }

    return QVariant();
}

QVariant MyTableModel::headerData(int section, Qt::Orientation orientation, int role) const {
    if (role != Qt::DisplayRole) {
        return QVariant();
    }

    if (orientation == Qt::Horizontal) {
        switch (section) {
            case 0:
                return tr("ID");
            case 1:
                return tr("Nume");
            case 2:
                return tr("Tip");
            case 3:
                return tr("Preț");
            case 4:
                return tr("Număr vocale");
            default:
                return QVariant();
        }
    }
    return QVariant();
}
