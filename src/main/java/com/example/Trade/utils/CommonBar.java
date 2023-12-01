package com.example.Trade.utils;

import com.example.Trade.dao.BarRepository;
import com.example.Trade.model.Bar;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.example.Trade.dao.BarRepository.ONE_MINUTE;
import static com.example.Trade.utils.DateTimeUtils.formatter;
import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
public class CommonBar {

    private final BarRepository barRepository;

    public CompletableFuture<Bar> getLastBarFeature() {
        // Получаем последний добавленный 1 минутный бар
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

    public CompletableFuture<List<Bar>> getListBarFeature(CompletableFuture<Bar> lastBarFeature, int count) {
        return lastBarFeature.thenApply(b -> {
            LocalDateTime lastDateTime = LocalDateTime.parse(b.getDateTime(), formatter);
            List<String> listDateTime = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                String dateTime = lastDateTime.minusMinutes(i).toString().split("\\.")[0]
                        .replace("T", " ").replace("-", ".") + ":00";
                listDateTime.add(dateTime);
            }
            return barRepository.findByIds(ONE_MINUTE, listDateTime);
        });
    }

}
