package com.example.Trade.service.impl;

import com.example.Trade.ReadAndWriteFile;
import com.example.Trade.dao.TickRepository;
import com.example.Trade.model.Tick;
import com.example.Trade.service.TickService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TickServiceImpl implements TickService {

    @Autowired
    TickRepository repository;

    @Override
    public void save(Tick tick) {
        repository.save(tick);
    }

    @Override
    public Tick findById(String id) {
        return repository.findById(id);
    }

    @Override
    public Map<String, Tick> findAll() {
        return repository.findAll();
    }

    @Override
    public void saveAll(Map<String, Tick> map) {
        repository.saveAll(map);
        System.out.println("we are save map Ticks");
    }

    @Override
    public void createTick(Path path) {
        List<String[]> collect = ReadAndWriteFile.readFile(path).stream()
                .map(str -> str.split("\\s+")).collect(Collectors.toList());

        Map<String, Tick> mapTicks = collect.stream()
                .map(arr -> new Tick(Integer.parseInt(arr[1]), Double.parseDouble(arr[2]),
                        arr[3], arr[4], arr[5], arr[6].concat(arr[3]), 1, null))
                .collect(Collectors.toMap(Tick::getMls, tick -> tick, (old, newValue) -> {
                    newValue.setCount(newValue.getCount() + old.getCount());
                    newValue.setVolume(newValue.getVolume() + old.getVolume());
                    return newValue;
                }));

        saveAll(mapTicks);
    }
}
