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
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.example.Trade.FileOperations.getListTickFromFile;
import static com.example.Trade.dao.BarRepository.ONE_MINUTE;
import static com.example.Trade.dao.BarRepository.ONE_SECONDS;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
public class BarOneMinuteJob {

    private static double open = 0.0;
    private static double close = 0.0;
    private static double high = 0.0;
    private static double low = 0.0;
    private static int volumeBuy = 0;
    private static int volumeSell = 0;
    private static int minuteStatic = -1;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");
    private static final AtomicInteger count = new AtomicInteger(0);

    private static final ThreadLocal<String> threadLocal = new ThreadLocal<>();

    @Autowired
    private BarRepository barRepository;

    /**
     * Получение 1 секундных баров из тиков и сохранение их в бд
     */
    @Scheduled(fixedDelay = 1300)
    public void createBarOneMinute() {
        CompletableFuture<Bar> barCompletableFuture = getBarCompletableFuture();

        List<Bar> barOneSeconds = barRepository.findAll(ONE_SECONDS).stream()
                .sorted(Comparator.comparing(Bar::getDateTime))
                .collect(Collectors.toList());

        Bar join = barCompletableFuture.join();
        if (nonNull(join)) {
            barOneSeconds = barOneSeconds.stream()
                    .filter(bar -> bar.getDateTime().compareTo(join.getDateTime()) >= 0)
                    .collect(Collectors.toList());
        }
        Map<String, Bar> barMap = createBarOneMinute(barOneSeconds).stream()
                .collect(Collectors.toMap(Bar::getDateTime, b -> b, (b1, b2) -> b2));
        barRepository.addAll(ONE_MINUTE, barMap);
    }

    private CompletableFuture<Bar> getBarCompletableFuture() {
        // Получаем последний добавленный бар
        return CompletableFuture.supplyAsync(() -> {
            int i = 0;
            while (i < 300) {
                String dateTime = LocalDateTime.now().minusHours(3).minusMinutes(i++).toString().split("\\.")[0]
                        .replace("T", " ").replace("-", ".").substring(0, 17) + "00";
                Bar bar = barRepository.findById(ONE_MINUTE, dateTime);
                if (nonNull(bar)) {
                    return bar;
                }
            }
            return null;
        });
    }

    public List<Bar> createBarOneMinute(List<Bar> bars) {
        List<Bar> barList = new ArrayList<>();
        count.set(0);
        bars
            .forEach(bos -> {
                count.incrementAndGet();
                String[] splitDateTime = bos.getDateTime().split(" ");
                String[] splitTime = splitDateTime[1].split(":");
                int minute = Integer.parseInt(splitTime[1]);

                if (minuteStatic < 0) {
                    minuteStatic = minute;
                }
                if (minuteStatic == minute) {
                    volumeBuy += bos.getVolumeBuy();
                    volumeSell += bos.getVolumeSell();

                    // get high price
                    if (high < bos.getHigh()) {
                        high = bos.getHigh();
                    }

                    // get low price
                    if (low > bos.getLow()) {
                        low = bos.getLow();
                    }

                    close = bos.getClose();
                }
                if (minuteStatic != minute || bars.size() == count.get()) {
                    //todo проверить или еще лучше написать тест

                    String dateTime;
                    if (splitTime[1].equals("00")) {
                        dateTime = LocalDateTime.parse(bos.getDateTime(), formatter).minusMinutes(5).toString().replace("T", " ");
                    } else {
                        dateTime = splitDateTime[0] + " " + splitTime[0] + ":" + (String.valueOf(minuteStatic).length() == 1 ? "0" + minuteStatic : minuteStatic) + ":00";
                    }
                    String test = bos.getDateTime();
                    Bar bar = Bar.builder()
                            .open(open)
                            .close(close)
                            .high(high)
                            .low(low)
                            .dateTime(dateTime)
                            .volumeBuy(volumeBuy)
                            .volumeSell(volumeSell)
                            .symbol(bos.getSymbol())
                            .interval(60)
                            .build();

//                    if (nonNull(lastBar) && (lastBar.getVolumeBuy() < bar.getVolumeBuy() || lastBar.getVolumeSell() < bar.getVolumeSell())) {
//                        return;
//                    }
                    barList.add(bar);

                    open = bos.getOpen();
                    close = bos.getOpen();
                    high = bos.getOpen();
                    low = bos.getOpen();
                    volumeBuy = bos.getVolumeBuy();
                    volumeSell = bos.getVolumeSell();
                    minuteStatic = minute;
                }
            });
        return barList;
    }
}
