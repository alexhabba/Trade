package com.example.Trade;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class MainTest {
    public static void main(String[] args) {
        //2023.11.14 11:37:36
        String res1 = LocalDateTime.now().minusSeconds(3).toString().split("\\.")[0].replace("T", " ").replace("-", ".");
        //2023.11.14 11:37:33
        String res2 = LocalDateTime.now().minusSeconds(6).toString().split("\\.")[0].replace("T", " ").replace("-", ".");
        System.out.println(res1);
        System.out.println(LocalTime.now().minusSeconds(5).toString());

//        tick > bar
        System.out.println(res1.compareTo(res2) > 0);

        String str = "14:05:54";
        System.out.println(str.contains("14:05"));
    }
}
