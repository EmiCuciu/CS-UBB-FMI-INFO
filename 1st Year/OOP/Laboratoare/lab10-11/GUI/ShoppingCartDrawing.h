

#ifndef LAB10_11_SHOPPINGCARTDRAWING_H
#define LAB10_11_SHOPPINGCARTDRAWING_H

#pragma once

#include "../Service/Service.h"

#include <QDialog>
#include <QPainter>
#include <QVBoxLayout>
#include <QLabel>
#include <QPaintEvent>
#include <random>

class ShoppingCartDrawing : public QDialog, public Observer {
private:
    Service &service;
    QLabel *count_label;
    QVBoxLayout *main_layout;

    const int CIRCLE_SIZE = 20;
    const int PENCIL_WIDTH = 5;

public:
    explicit ShoppingCartDrawing(Service &service, QWidget *parent = nullptr)
            : QDialog(parent), service(service) {
        service.addListener(this);
        main_layout = new QVBoxLayout(this);
        count_label = new QLabel("Number of circles: 0");
        main_layout->addWidget(count_label);
    }

    void paintEvent(QPaintEvent *event) override {
        QPainter painter(this);
        auto const &cart = service.getAllCos();

        std::mt19937 gen(std::random_device{}());
        std::uniform_int_distribution<int> distributionX(0, width());
        std::uniform_int_distribution<int> distributionY(0, height());

        for (const auto &medicament: cart) {
            painter.setPen(QPen(Qt::blue, PENCIL_WIDTH));
            painter.setBrush(QBrush{Qt::blue, Qt::SolidPattern});

            int x = distributionX(gen);
            int y = distributionY(gen);

            painter.drawEllipse(x, y, CIRCLE_SIZE, CIRCLE_SIZE);
        }

        count_label->setText("Number of circles: " + QString::number(cart.size()));
    }

    void update() override {
        repaint();
    }

protected:
    void closeEvent(QCloseEvent *event) override {
        service.deleteListener(this);
    }
};

#endif //LAB10_11_SHOPPINGCARTDRAWING_H
