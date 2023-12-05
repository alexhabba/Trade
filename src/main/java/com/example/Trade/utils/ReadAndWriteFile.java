package com.example.Trade.utils;

import org.apache.log4j.Logger;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ReadAndWriteFile {

    private static final Logger LOG = Logger.getLogger(ReadAndWriteFile.class);

    public static void writeFile(Path path, byte[] bytes) {
        try {
            Files.write(path, bytes);
        } catch (IOException e) {
            LOG.error("Не удалось записать в файл path = " + path, e);
        }
    }

    public static void writeFileAppend(Path path, String operation) {
        try {
//            Files.write(path, bytes, StandardCharsets.UTF_16LE);
            Files.write(path, Collections.singleton(operation), StandardCharsets.UTF_16LE);
        } catch (IOException e) {
            LOG.error("Не удалось записать в файл path = " + path, e);
        }

//        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
//                new FileOutputStream("/Users/alex/Library/Application Support/net.metaquotes.wine.metatrader5/drive_c/Program Files/MetaTrader 5/MQL5/Files/Research/testRead.txt"), StandardCharsets.UTF_16BE))) {
//            writer.write("buy");
//        } catch (IOException ex) {
//            // Report
//        }
        /*ignore*/
    }

    public static List<String> readFile(Path path) {
        try {
            return Files.lines(path, StandardCharsets.UTF_16).collect(Collectors.toList());
        } catch (IOException e) {
            LOG.debug("Не удалось прочитать файл path = " + path);
            return List.of();
        }
    }
}
