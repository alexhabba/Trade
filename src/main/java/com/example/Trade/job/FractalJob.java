package com.example.Trade.job;

import com.example.Trade.dao.FractalRepository;
import com.example.Trade.model.Bar;
import com.example.Trade.model.Fractal;
import com.example.Trade.utils.CommonBar;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import static com.example.Trade.utils.ReadAndWriteFile.writeFileAppend;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class FractalJob {

    private final FractalRepository fractalRepository;
    private final CommonBar commonBar;

    @Scheduled(fixedDelay = 60000)
    @SneakyThrows
    private void someMethod() {
        // чтобы сформировать фрактал, нужно слева и справа иметь одинаковое кол-во баров
        // фрактал 13 баров берем кол-во баров умножаем на 2 и плюс 2
        int count = 28;
        CompletableFuture<List<Bar>> listBarFeature = commonBar.getListBarFeature(commonBar.getLastBarFeature(), count);
        List<Bar> bars = listBarFeature.join().stream()
                .filter(Objects::nonNull)
                .sorted(comparing(Bar::getDateTime))
                .limit(count - 1)
                .collect(toList());

        int median = bars.size() / 2;
        Bar bar = bars.get(median);

        if (isLowFractalLeft(bars, median) && isLowFractalRight(bars, median)) {
            Fractal fractal = Fractal.builder()
                    .countBar(count)
                    .dateTime(bar.getDateTime())
                    .interval(60)
                    .lowFractalPrice(bar.getLow())
                    .build();
            fractalRepository.add(FractalRepository.FRACTAL_ONE_MINUTE, fractal);
            writeFileAppend(Path.of("/Users/alex/Library/Application Support/net.metaquotes.wine.metatrader5/drive_c/Program Files/MetaTrader 5/MQL5/Files/Research/testRead.txt"), "sell");
        }

        if (isHighFractalLeft(bars, median) && isHighFractalRight(bars, median)) {
            Fractal fractal = Fractal.builder()
                    .countBar(count)
                    .dateTime(bar.getDateTime())
                    .interval(60)
                    .highFractalPrice(bar.getHigh())
                    .build();
            fractalRepository.add(FractalRepository.FRACTAL_ONE_MINUTE, fractal);
            writeFileAppend(Path.of("/Users/alex/Library/Application Support/net.metaquotes.wine.metatrader5/drive_c/Program Files/MetaTrader 5/MQL5/Files/Research/testRead.txt"), "buy");
        }
    }

    private boolean isLowFractalLeft(List<Bar> bars, int median) {
        for (int i = 0; i < median; i++) {
            if (!(bars.get(median).getLow() <= bars.get(i).getLow())) {
                return false;
            }
        }
        return true;
    }

    private boolean isLowFractalRight(List<Bar> bars, int median) {
        for (int i = median + 1; i < bars.size(); i++) {
            if (!(bars.get(median).getLow() < bars.get(i).getLow())) {
                return false;
            }
        }
        return true;
    }

    private boolean isHighFractalLeft(List<Bar> bars, int median) {
        for (int i = 0; i < median; i++) {
            if (!(bars.get(median).getHigh() >= bars.get(i).getHigh())) {
                return false;
            }
        }
        return true;
    }

    private boolean isHighFractalRight(List<Bar> bars, int median) {
        for (int i = median + 1; i < bars.size(); i++) {
            if (!(bars.get(median).getHigh() > bars.get(i).getHigh())) {
                return false;
            }
        }
        return true;
    }
}
