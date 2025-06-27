package com.services;

import com.model.Game;

import java.util.List;

public interface GameObserver {
    void rankingUpdated(List<Game> newRanking);
}