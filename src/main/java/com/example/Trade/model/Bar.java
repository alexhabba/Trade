package com.example.Trade.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Bar implements Serializable {

    private static final long serialVersionUID = 1L;

    private double   open;
    private double   close;
    private double   high;
    private double   low;
    private int      volumeBuy;
    private int      volumeSell;
    private Symbol   symbol;
    private String dateTime;
    private int interval;
}
