//
// Created by Emi on 4/20/2024.
//

#include "undo.h"


UndoAdauga::UndoAdauga(Librarie &librarie, const Carte& carte) : carte{carte}, librarie{librarie} {}

void UndoAdauga::doUndo() {
    auto position = librarie.findCarte(carte.getTitlu());
    librarie.deleteCarte(position);
}

UndoSterge::UndoSterge(Librarie &librarie, const Carte& carte) : carte{carte}, librarie{librarie} {}

void UndoSterge::doUndo() {
    librarie.addCarte(carte);
}

UndoUpdate::UndoUpdate(Librarie &librarie, const Carte& carte) : carte{carte}, librarie{librarie} {}

void UndoUpdate::doUndo() {
    auto position = librarie.findCarte(carte.getTitlu());
    Librarie::updateCarte(position, carte);
}