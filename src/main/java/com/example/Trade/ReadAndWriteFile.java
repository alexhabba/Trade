package com.example.Trade;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Collectors;

public class ReadAndWriteFile {

    public static void writeFile(Path path, byte[] bytes) {
        try {
            Files.write(path, bytes);
        } catch (IOException e) {
            System.out.println("Ошибка записи в файл");
        }
    }

    public static void writeFileAppend(Path path, byte[] bytes) {
        try {
            Files.write(path, bytes, StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.out.println("Ошибка записи в файл");
        }
    }

    public static List<String> readFile(Path path) {
        try {
            byte[] bytes = Files.readAllBytes(path);
//            return Files.readAllLines(path);
//            return Arrays.asList((new String(bytes, StandardCharsets.UTF_16)).split("\\n"));
            return Files.lines(path, StandardCharsets.UTF_16).collect(Collectors.toList());
        } catch (IOException e) {
            System.out.println("Ошибка чтения файла");
            return List.of();
        }
    }
}
