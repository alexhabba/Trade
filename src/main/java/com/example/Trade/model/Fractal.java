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
public class Fractal implements Serializable {

    private static final long serialVersionUID = 1L;

    private String dateTime;
    private double highFractalPrice;
    private double lowFractalPrice;
    private int interval;
    private int countBar;
}
