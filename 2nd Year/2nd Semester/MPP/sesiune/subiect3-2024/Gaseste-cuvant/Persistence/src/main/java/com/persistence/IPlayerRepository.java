package com.persistence;

import com.model.Player;

public interface IPlayerRepository extends IRepository<Integer, Player> {
    Player findByAlias(String alias);
}
