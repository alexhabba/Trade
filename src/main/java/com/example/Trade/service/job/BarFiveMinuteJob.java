package com.example.Trade.service.job;

import com.example.Trade.dao.BarRepository;
import com.example.Trade.model.Bar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.example.Trade.dao.BarRepository.FIVE_MINUTE;
import static com.example.Trade.dao.BarRepository.ONE_MINUTE;
import static com.example.Trade.dao.BarRepository.ONE_SECONDS;

@Service
public class BarFiveMinuteJob {

    private static double open = 0.0;
    private static double close = 0.0;
    private static double high = 0.0;
    private static double low = 0.0;
    private static int volumeBuy = 0;
    private static int volumeSell = 0;
    private static int minuteStatic = 0;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");
    private static final AtomicInteger count = new AtomicInteger(0);

    private static final ThreadLocal<String> threadLocal = new ThreadLocal<>();

    @Autowired
    private BarRepository barRepository;

    /**
     * Получение 1 секундных баров из тиков и сохранение их в бд
     */
    @Scheduled(fixedDelay = 1300)
    public void createBarFiveMinute() {
        List<Bar> barOneSeconds = barRepository.findAll(ONE_MINUTE).stream()
                .sorted(Comparator.comparing(Bar::getDateTime))
                .collect(Collectors.toList());
        Map<String, Bar> barMap = createBarOneMinute(barOneSeconds, 5).stream()
                .collect(Collectors.toMap(Bar::getDateTime, b -> b, (b1, b2) -> b1));

        barMap.values()
                .forEach(bar -> barRepository.add(FIVE_MINUTE, bar));
    }

    public List<Bar> createBarOneMinute(List<Bar> bars, int interval) {
        List<Bar> barList = new ArrayList<>();
        count.set(0);
        bars
            .forEach(bos -> {
                count.incrementAndGet();
                String[] splitDateTime = bos.getDateTime().split(" ");
                String[] splitTime = splitDateTime[1].split(":");
                int minute = Integer.parseInt(splitTime[1]);

                if (minute % interval != 0) {
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
                if (minute % interval == 0 || bars.size() == count.get()) {
                    String dateTime;
                    if (splitTime[1].equals("00")) {
                        dateTime = LocalDateTime.parse(bos.getDateTime(), formatter).minusMinutes(5).toString().replace("T", " ");
                    } else {
                        dateTime = splitDateTime[0] + " " + splitTime[0] + ":" + (String.valueOf(minuteStatic).length() == 1 ? "0" + minuteStatic : minuteStatic) + ":00";
                    }
                    Bar bar = Bar.builder()
                            .open(open)
                            .close(close)
                            .high(high)
                            .low(low)
                            .dateTime(dateTime)
                            .volumeBuy(volumeBuy)
                            .volumeSell(volumeSell)
                            .symbol(bos.getSymbol())
                            .interval(interval * 60)
                            .build();

                    barList.add(bar);

                    if (minute % interval != 0) {
                        close = bos.getClose();
                        if (high < bos.getHigh()) {
                            high = bos.getHigh();
                        }

                        // get low price
                        if (low > bos.getLow()) {
                            low = bos.getLow();
                        }

                        volumeBuy += bos.getVolumeBuy();
                        volumeSell += bos.getVolumeSell();
                    } else {
                        open = bos.getOpen();
                        close = bos.getOpen();
                        high = bos.getOpen();
                        low = bos.getOpen();
                        volumeBuy = bos.getVolumeBuy();
                        volumeSell = bos.getVolumeSell();
                        minuteStatic = minute;
                    }
                }
            });
        return barList;
    }
}
