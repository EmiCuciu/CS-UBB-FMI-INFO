package com.persistence;


import com.model.Joc;
import com.network.dto.JocNeghicitDTO;

import java.util.List;

public interface IRepoJocuri extends IRepository<Integer, Joc> {

    List<JocNeghicitDTO> findJocuriNeghicite(int id);
}
