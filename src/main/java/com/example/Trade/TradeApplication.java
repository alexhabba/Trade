package com.example.Trade;

import com.example.Trade.service.TickService;
import com.example.Trade.service.impl.TickServiceImpl;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.nio.file.Path;

@SpringBootApplication
public class TradeApplication {

	public static void main(String[] args) throws InterruptedException {
		String log4jConfPath = "log4j.properties";
		PropertyConfigurator.configure(log4jConfPath);
		ConfigurableApplicationContext context = SpringApplication.run(TradeApplication.class, args);
		TickService tickService = context.getBean("tickServiceImpl", TickServiceImpl.class);
		while (true) {
			Path path = Path.of(args[0]);
			tickService.createTick(path);
			Thread.sleep(1300);
		}
	}

}
