package com.example.Trade.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Data
@Builder
public class Tick implements Serializable {

    private static final long serialVersionUID = 1L;

    private int      volume;
    private double   price;
    private String   operation;
    private String   date;
    private String   time;
    private String   symbol;
}
