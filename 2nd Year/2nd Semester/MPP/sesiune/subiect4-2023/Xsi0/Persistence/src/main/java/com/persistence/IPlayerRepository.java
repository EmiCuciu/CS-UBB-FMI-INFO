package com.persistence;

import com.model.Player;

import java.util.Optional;

public interface IPlayerRepository extends Repository<Long, Player> {

    Optional<Player> findOneByAlias(String alias);
}
