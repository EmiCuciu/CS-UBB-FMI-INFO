package com.persistence;

import com.model.Game;
import com.model.Player;

public interface IGameRepository extends Repository<Long, Game> {

    Iterable<Game> findAllByPlayer(Player player);
}
