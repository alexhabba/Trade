package com.example.Trade.service.impl;

import com.example.Trade.ReadAndWriteFile;
import com.example.Trade.dao.TickRepository;
import com.example.Trade.model.Tick;
import com.example.Trade.service.TickService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TickServiceImpl implements TickService {

    private static final Logger LOG = Logger.getLogger(TickServiceImpl.class);

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
        LOG.info("зашли в метод findAll");
        Map<String, Tick> all = repository.findAll();
        LOG.info(String.format("получили Map<String, Tick> из %d елементов", all.size()));
        return all;
    }

    @Override
    public void saveAll(Map<String, Tick> map) {
        LOG.info("зашли в метод saveAll");
        repository.saveAll(map);
        LOG.info("сохранили Map<String, Tick> в redis");
    }

    @Override
    public void createTick(Path path) {
        LOG.info("зашли в метод createTick");

        List<String[]> collect = ReadAndWriteFile.readFile(path).stream()
                .map(str -> str.split("\\s+")).collect(Collectors.toList());

        LOG.info(String.format("получили список из %d строк", collect.size()));

        Map<String, Tick> mapTicks = collect.stream()
                .filter(arr -> arr.length == 7)
                .map(arr -> new Tick(Integer.parseInt(arr[1]), Double.parseDouble(arr[2]),
                        arr[3], arr[4], arr[5], arr[6].concat(arr[3]), 1, null))
                .collect(Collectors.toMap(Tick::getMls, tick -> tick, (old, newValue) -> {
                    newValue.setCount(newValue.getCount() + old.getCount());
                    newValue.setVolume(newValue.getVolume() + old.getVolume());
                    return newValue;
                }));

        LOG.info(String.format("получили мапу из %d елементов", mapTicks.size()));

        saveAll(mapTicks);
    }
}
