package com.example.Trade.model;

import java.io.Serializable;

public class Bar implements Serializable {

    private static final long serialVersionUID = 1L;

    private double   open;
    private double   close;
    private double   high;
    private double   low;
    private int      volume;
    private int      volumeBuy;
    private int      volumeSell;
    private int      period;
    private String   symbol;
    private String   date;
    private String   time;
    private String   mls;

    public double getOpen() {
        return open;
    }

    public Bar setOpen(double open) {
        this.open = open;
        return this;
    }

    public double getClose() {
        return close;
    }

    public Bar setClose(double close) {
        this.close = close;
        return this;
    }

    public double getHigh() {
        return high;
    }

    public Bar setHigh(double high) {
        this.high = high;
        return this;
    }

    public double getLow() {
        return low;
    }

    public Bar setLow(double low) {
        this.low = low;
        return this;
    }

    public int getVolume() {
        return volume;
    }

    public Bar setVolume(int volume) {
        this.volume = volume;
        return this;
    }

    public int getVolumeBuy() {
        return volumeBuy;
    }

    public Bar setVolumeBuy(int volumeBuy) {
        this.volumeBuy = volumeBuy;
        return this;
    }

    public int getVolumeSell() {
        return volumeSell;
    }

    public Bar setVolumeSell(int volumeSell) {
        this.volumeSell = volumeSell;
        return this;
    }

    public int getPeriod() {
        return period;
    }

    public Bar setPeriod(int period) {
        this.period = period;
        return this;
    }

    public String getSymbol() {
        return symbol;
    }

    public Bar setSymbol(String symbol) {
        this.symbol = symbol;
        return this;
    }

    public String getDate() {
        return date;
    }

    public Bar setDate(String date) {
        this.date = date;
        return this;
    }

    public String getTime() {
        return time;
    }

    public Bar setTime(String time) {
        this.time = time;
        return this;
    }

    public String getMls() {
        return mls;
    }

    public Bar setMls(String mls) {
        this.mls = mls;
        return this;
    }

    @Override
    public String toString() {
        return "Bar{" +
                "open=" + open +
                ", close=" + close +
                ", high=" + high +
                ", low=" + low +
                ", volume=" + volume +
                ", volumeBuy=" + volumeBuy +
                ", volumeSell=" + volumeSell +
                ", period=" + period +
                ", symbol='" + symbol + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", mls='" + mls + '\'' +
                '}';
    }
}
