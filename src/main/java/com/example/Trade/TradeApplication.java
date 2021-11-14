package com.example.Trade;

import com.example.Trade.service.TickService;
import com.example.Trade.service.impl.TickServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.nio.file.Path;

@SpringBootApplication
public class TradeApplication {

	private static final org.apache.log4j.Logger LOG = Logger.getLogger(TradeApplication.class);

	public static void main(String[] args) {
		LOG.error("очень странно все");
		ConfigurableApplicationContext context = SpringApplication.run(TradeApplication.class, args);
		TickService tickService = context.getBean("tickServiceImpl", TickServiceImpl.class);
		Path path = Path.of(args[0]);
		tickService.createTick(path);
	}

}
