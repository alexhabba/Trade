package com.example.Trade;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
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

    public static void writeFileAppend(Path path, byte[] bytes) {
        try {
            Files.write(path, bytes, StandardOpenOption.APPEND);
        } catch (IOException e) {
            LOG.error("Не удалось записать в файл path = " + path, e);
        }
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
