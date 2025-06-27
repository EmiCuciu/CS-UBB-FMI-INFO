package com.services;

import com.model.*;

public interface IObserver {

    void gameAdded(Game game) throws AppException;
}
