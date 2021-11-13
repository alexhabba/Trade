package com.example.Trade.dao;

import com.example.Trade.model.Tick;

public interface TickRepository {

    void save(Tick tick);

    Tick findById(String id);

    void delete(String id);
}
