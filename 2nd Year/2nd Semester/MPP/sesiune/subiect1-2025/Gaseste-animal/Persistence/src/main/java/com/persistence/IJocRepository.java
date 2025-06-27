package com.persistence;

import com.model.Joc;
import com.model.Player;

public interface IJocRepository extends IRepository<Integer, Joc> {
    Iterable<Joc> findAllByPlayer(Player player);
}
