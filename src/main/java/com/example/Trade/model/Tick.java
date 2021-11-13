package com.example.Trade.model;

import java.io.Serializable;
import java.util.Objects;

public class Tick implements Serializable {

    private static final long serialVersionUID = 1L;

    private int      volume;
    private double   price;
    private String   operation;
    private String   date;
    private String   time;
    private String   mls;
    private int      count;
    private String   symbol;

    public Tick(int volume, double price, String operation, String date, String time, String mls, int count, String symbol) {
        this.volume = volume;
        this.price = price;
        this.operation = operation;
        this.date = date;
        this.time = time;
        this.mls = mls;
        this.count = count;
        this.symbol = symbol;
    }

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

    public double getPrice() {
        return price;
    }

    public Tick setPrice(double price) {
        this.price = price;
        return this;
    }

    public String getOperation() {
        return operation;
    }

    public Tick setOperation(String operation) {
        this.operation = operation;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tick)) return false;
        Tick tick = (Tick) o;
        return getVolume() == tick.getVolume() && Double.compare(tick.price, price) == 0 && getCount() == tick.getCount() && Objects.equals(operation, tick.operation) && Objects.equals(getDate(), tick.getDate()) && Objects.equals(getTime(), tick.getTime()) && Objects.equals(getMls(), tick.getMls()) && Objects.equals(getSymbol(), tick.getSymbol());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getVolume(), price, operation, getDate(), getTime(), getMls(), getCount(), getSymbol());
    }

    @Override
    public String toString() {
        return "Tick{" +
                "volume=" + volume +
                ", price=" + price +
                ", operation='" + operation + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", mls='" + mls + '\'' +
                ", count=" + count +
                ", symbol='" + symbol + '\'' +
                '}';
    }
}
