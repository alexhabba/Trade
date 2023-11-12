package com.example.Trade.controller;

import com.example.Trade.dao.BarRepository;
import com.example.Trade.model.Bar;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.Trade.dao.BarRepository.ONE_SECONDS;

@RestController
@RequiredArgsConstructor
public class TickController {

    private static final Logger LOG = Logger.getLogger(TickController.class);

    @Autowired
    private BarRepository repository;

    @GetMapping("/getAll")
    public ResponseEntity<List<Bar>> findAll(@RequestParam String key) {
        LOG.info("get all ticks from the redis.");
        return ResponseEntity.ok(repository.findAll(key).stream()
                .sorted(Comparator.comparing(Bar::getDateTime))
                .collect(Collectors.toList()));
    }

    @GetMapping("/getAllSize")
    public ResponseEntity<List<Bar>> findAllLimit(@RequestParam String key, @RequestParam int size) {
        LOG.info("get all ticks from the redis.");
        List<Bar> collect = repository.findAll(key).stream()
                .sorted(Comparator.comparing(Bar::getDateTime))
                .collect(Collectors.toList());
        collect = collect.stream()
                .skip(collect.size() - size)
                .collect(Collectors.toList());
        return ResponseEntity.ok(collect);
    }
}
