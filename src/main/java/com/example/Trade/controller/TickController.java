package com.example.Trade.controller;

import com.example.Trade.model.Tick;
import com.example.Trade.service.TickService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class TickController {

	private static final Logger LOG = LoggerFactory.getLogger(TickController.class);

	@Autowired
	TickService tickService;

	// Get all tics.
	// Url - http://localhost:8080/getall
	@GetMapping("/getAll")
	public Map<String, Tick> findAll() {
		LOG.info("Fetching all employees from the redis.");
		Map<String, Tick> tickMap = tickService.findAll();
		System.out.println("в мапе столко елементов   =  " + tickMap.size());
		return tickMap;
	}

}
