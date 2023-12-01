package com.example.Trade.controller;

import com.example.Trade.dao.FractalRepository;
import com.example.Trade.model.Fractal;
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

@RestController
@RequiredArgsConstructor
public class FractalController {

    private static final Logger LOG = Logger.getLogger(FractalController.class);

    @Autowired
    private FractalRepository repository;

    @GetMapping("/getFractalAll")
    public ResponseEntity<List<Fractal>> findAll(@RequestParam String key) {
        LOG.info("get all ticks from the redis.");
        return ResponseEntity.ok(repository.findAll(key).stream()
                .sorted(Comparator.comparing(Fractal::getDateTime))
                .collect(Collectors.toList()));
    }

    @GetMapping("/fractalSize")
    public ResponseEntity<List<Fractal>> findAllLimit(@RequestParam String key, @RequestParam int size) {
        LOG.info("get all ticks from the redis.");
        List<Fractal> collect = repository.findAll(key).stream()
                .sorted(Comparator.comparing(Fractal::getDateTime))
                .collect(Collectors.toList());
        collect = collect.stream()
                .skip(collect.size() - size)
                .collect(Collectors.toList());
        return ResponseEntity.ok(collect);
    }
}
