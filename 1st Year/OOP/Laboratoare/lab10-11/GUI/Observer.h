#ifndef LAB10_11_OBSERVER_H
#define LAB10_11_OBSERVER_H

#include <vector>
#include <algorithm>

class Observer {
public:
    virtual void update() = 0;
};

class Subject {
private:
    std::vector<Observer *> listeners;
protected:
    void notify() {
        for (auto l: listeners) {
            l->update();
        }
    }

public:

    void addListener(Observer *obs) {
        listeners.push_back(obs);
    }

    void deleteListener(Observer *obs) {
        listeners.erase(std::remove(listeners.begin(), listeners.end(), obs), listeners.end());
    }
};

#endif //LAB10_11_OBSERVER_H
