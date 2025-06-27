package com.persistence;

import com.model.Game;
import com.model.Position;

public interface IPositionRepository extends Repository<Long, Position> {

    Iterable<Position> findAllByGame(Game game);
}
