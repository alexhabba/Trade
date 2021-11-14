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

	// Get all tics.
	// Url - http://localhost:8080/getall
	@GetMapping("/getAll")
	public Map<String, Tick> findAll() {
		System.out.println(getClass().getClassLoader().getResource("logging.properties"));
		LOG.info("Fetching all employees from the redis.");
		LOG.error("sdfasdgfdfgdsfgdsfgdfgsdf");
		Map<String, Tick> tickMap = tickService.findAll();
		System.out.println("в мапе столко елементов   =  " + tickMap.size());
		return tickMap;
	}

}
