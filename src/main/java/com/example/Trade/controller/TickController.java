package com.example.Trade.controller;

import com.example.Trade.model.Tick;
import com.example.Trade.service.TickService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class TickController {

	private static final Logger LOG = Logger.getLogger(TickController.class);

	@Autowired
	TickService tickService;

	// Url - http://localhost:8080/getall
	@GetMapping("/getAll")
	public Map<String, Tick> findAll() {
		LOG.info("get all ticks from the redis.");
		return tickService.findAll();
	}

}
