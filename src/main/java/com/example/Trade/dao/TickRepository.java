package com.example.Trade.dao;

import com.example.Trade.model.Tick;

import java.util.Map;

public interface TickRepository {

    void save(Tick tick);

    Tick findById(String id);

    Map<String, Tick> findAll();

    void delete(String id);

    void saveAll(Map<String, Tick> map);

    long getSize();

}
