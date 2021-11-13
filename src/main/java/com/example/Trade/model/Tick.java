package com.example.Trade.model;

import java.io.Serializable;

public class Tick implements Serializable {

    private static final long serialVersionUID = 1L;

    private int      volume;
    private int      count;
    private String   symbol;
    private String   date;
    private String   time;
    private String   mls;

    public int getVolume() {
        return volume;
    }

    public Tick setVolume(int volume) {
        this.volume = volume;
        return this;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getCount() {
        return count;
    }

    public Tick setCount(int count) {
        this.count = count;
        return this;
    }

    public String getSymbol() {
        return symbol;
    }

    public Tick setSymbol(String symbol) {
        this.symbol = symbol;
        return this;
    }

    public String getDate() {
        return date;
    }

    public Tick setDate(String date) {
        this.date = date;
        return this;
    }

    public String getTime() {
        return time;
    }

    public Tick setTime(String time) {
        this.time = time;
        return this;
    }

    public String getMls() {
        return mls;
    }

    public Tick setMls(String mls) {
        this.mls = mls;
        return this;
    }

    @Override
    public String toString() {
        return "Tick{" +
                "volume=" + volume +
                ", count=" + count +
                ", symbol='" + symbol + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", mls='" + mls + '\'' +
                '}';
    }
}
