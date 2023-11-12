package com.example.Trade.service.job;

import com.example.Trade.dao.BarRepository;
import com.example.Trade.model.Bar;
import com.example.Trade.model.Symbol;
import com.example.Trade.model.Tick;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.example.Trade.FileOperations.getListTickFromFile;
import static com.example.Trade.dao.BarRepository.ONE_SECONDS;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
public class BarOneSecondsJob {

    private static double open = 0.0;
    private static double close = 0.0;
    private static double high = 0.0;
    private static double low = 0.0;
    private static int volumeBuy = 0;
    private static int volumeSell = 0;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");
    private static final AtomicInteger count = new AtomicInteger(0);

    private static final ThreadLocal<String> threadLocal = new ThreadLocal<>();

    @Value("${source.file}")
    private String path;

    @Autowired
    private BarRepository barRepository;

    /**
     * Получение 1 секундных баров из тиков и сохранение их в бд
     */
    @Scheduled(fixedDelay = 1300)
    public void createBarOneSeconds() {
        // Получаем последний добавленный бар
        CompletableFuture<Bar> barCompletableFuture = CompletableFuture.supplyAsync(() -> {
            int i = 1;
            while (i < 300) {
                String dateTime = LocalDateTime.now().minusHours(3).minusSeconds(i++).toString().split("\\.")[0].replace("T", " ").replace("-", ".");
                Bar bar = barRepository.findById(ONE_SECONDS, dateTime);
                if (nonNull(bar)) {
                    return bar;
                }
            }
            return null;
        });

        List<Tick> listTickFromFile = getListTickFromFile(Path.of(path));
        if (listTickFromFile.isEmpty()) {
            return;
        }
        Bar join = barCompletableFuture.join();
        int size = listTickFromFile.size();
        if (nonNull(join)) {
            listTickFromFile = listTickFromFile.stream()
                    .filter(tick -> (tick.getDate() + " " + tick.getTime()).compareTo(join.getDateTime()) >= 0)
                    .collect(Collectors.toList());
        }
        int size1 = listTickFromFile.size();

        Map<String, Bar> barMap = createBarOneSeconds(listTickFromFile)
                .stream()
                .collect(Collectors.toMap(Bar::getDateTime, b -> b, (b1, b2) -> b2));

        barRepository.addAll(ONE_SECONDS, barMap);
    }

    public List<Bar> createBarOneSeconds(List<Tick> ticks) {
        List<Bar> barList = new ArrayList<>();
        ticks
                .forEach(tick -> {
                    count.incrementAndGet();
                    String dateTime = new StringBuilder().append(tick.getDate()).append(" ").append(tick.getTime()).toString();
                    int vol = tick.getVolume();
                    double price = tick.getPrice();
                    String operation = tick.getOperation();

                    if (isNull(threadLocal.get())) {
                        threadLocal.set(dateTime);
                        open = price;
                        close = price;
                        high = price;
                        low = price;
                    }
                    if (dateTime.equals(threadLocal.get())) {

                        // объем
                        if (operation.equals("buy")) {
                            volumeBuy += vol;
                        } else {
                            volumeSell += vol;
                        }

                        // get high price
                        if (high < price) {
                            high = price;
                        }

                        // get low price
                        if (low > price) {
                            low = price;
                        }

                        close = price;
                    }
                    if (!dateTime.equals(threadLocal.get()) || ticks.size() == count.get()) {
                        Bar bar = Bar.builder()
                                .open(open)
                                .close(close)
                                .high(high)
                                .low(low)
                                .dateTime(threadLocal.get())
                                .volumeBuy(volumeBuy)
                                .volumeSell(volumeSell)
                                .symbol(Arrays.stream(Symbol.values()).filter(value -> value.getValue().equals(tick.getSymbol())).findAny().get())
                                .interval(1)
                                .build();

                        barList.add(bar);

                        threadLocal.set(dateTime);
                        open = price;
                        close = price;
                        high = price;
                        low = price;
                        if (operation.equals("buy")) {
                            volumeBuy = vol;
                            volumeSell = 0;
                        } else {
                            volumeSell = vol;
                            volumeBuy = 0;
                        }
                    }
                });

        return barList;
    }
}
