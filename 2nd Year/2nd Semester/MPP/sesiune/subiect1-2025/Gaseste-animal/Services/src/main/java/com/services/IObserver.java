package com.services;

import com.model.Joc;

public interface IObserver {

    void gameAdded(Joc game) throws AppException;
}
