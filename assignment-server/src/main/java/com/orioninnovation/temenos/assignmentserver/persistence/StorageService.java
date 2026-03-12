package com.orioninnovation.temenos.assignmentserver.persistence;

import com.orioninnovation.temenos.assignmentserver.model.Calculation;

public interface StorageService {
    void save(Calculation calculation);
    void update(Calculation calculation);
    Calculation get(String id);
}
