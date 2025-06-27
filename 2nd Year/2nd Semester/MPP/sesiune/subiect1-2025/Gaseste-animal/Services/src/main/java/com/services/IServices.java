package com.services;

import com.model.Joc;
import com.model.Player;
import com.model.Position;

import java.util.List;

public interface IServices {
    Player login(Player player, IObserver client) throws AppException;
    Joc addGame(Joc game) throws AppException;
    Position addPosition(Position position) throws AppException;
    Player findPlayerByAlias(String alias) throws AppException;
    List<Joc> getAllGames() throws AppException;
    Player logout(Player player, IObserver client) throws AppException;
}
