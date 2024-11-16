//
// Created by Emi on 4/20/2024.
//

#ifndef LAB8_UNDO_H
#define LAB8_UNDO_H

#include "../Repository/Repo.h"

class ActiuneUndo {
public:
    virtual void doUndo() = 0;

    virtual ~ActiuneUndo() = default;
};

class UndoAdauga : public ActiuneUndo {
private:
    Librarie &librarie;
    Carte carte;
public:
    UndoAdauga(Librarie &librarie, const Carte& carte);

    void doUndo() override;
};

class UndoSterge : public ActiuneUndo {
private:
    Librarie &librarie;
    Carte carte;
public:
    UndoSterge(Librarie &librarie, const Carte& carte);

    void doUndo() override;
};

class UndoUpdate : public ActiuneUndo {
private:
    Librarie &librarie;
    Carte carte;
public:
    UndoUpdate(Librarie &librarie, const Carte& carte);

    void doUndo() override;
};

#endif //LAB8_UNDO_H
