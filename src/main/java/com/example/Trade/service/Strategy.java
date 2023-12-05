package com.example.Trade.service;

import com.example.Trade.dao.BarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Strategy {

    private final BarRepository barRepository;


}
