package com.example.Trade.service.impl;

import com.example.Trade.ReadAndWriteFile;
import com.example.Trade.dao.TickRepository;
import com.example.Trade.model.Tick;
import com.example.Trade.service.TickService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TickServiceImpl implements TickService {

    @Value("${source.file}")
    private String source;

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

    @Scheduled(fixedDelay = 1300)
    public void createTick() {
        LOG.info("зашли в метод createTick " + Thread.currentThread().getName());

        List<String[]> collect = ReadAndWriteFile.readFile(Path.of(source)).stream()
                .map(str -> str.split("\\s+")).collect(Collectors.toList());

        LOG.info(String.format("получили список из %d строк", collect.size()));

        Map<String, Tick> mapTicks = collect.stream()
                .filter(arr -> arr.length == 7)
                .filter(arr -> arr[6].length() == 13)
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

    @Override
    public void delete(String id) {
        repository.delete(id);
    }

    @Override
    public long getSize() {
        return repository.getSize();
    }

    @Scheduled(fixedDelay = 130000)
    public void deleteElementRedis() {
        LOG.info("зашли в метод deleteElementRedis " + Thread.currentThread().getName());
        if (getSize() > 100_000) {
            Map<String, Tick> map = findAll();

            List<Tick> sortedList = map.values().stream()
                    .filter(v -> v.getMls().length() == 14)
                    .map(v -> v.setMls(v.getMls().substring(0, 13)))
                    .sorted(Comparator.comparing(Tick::getMls))
                    .collect(Collectors.toList());

            List<Tick> removeElement = sortedList.stream()
                    .limit(map.size() - 100000)
                    .collect(Collectors.toList());

            List<String> collect = removeElement.stream()
                    .map(t -> t.getMls() + t.getOperation())
                    .collect(Collectors.toList());

            collect.forEach(this::delete);
        }
    }
}
