package com.chepiv;

import com.chepiv.algorithm.SimulatedAnnealing;
import com.github.sh0nk.matplotlib4j.PythonExecutionException;

import java.io.IOException;

/**
 * Created by chepiv on 30/10/2019.
 * Contact: chepurin.ivan@gmail.com
 * Github:chepiv
 */
public class SAMain {
    public static void main(String[] args) throws IOException, PythonExecutionException {
        double temperature = 100000;
        double coolingRate = 0.00003;
        int startingCity = 0;
        String fileName = "data/kroA100.tsp";
        SimulatedAnnealing simulatedAnnealing = new SimulatedAnnealing(fileName,temperature,0,coolingRate);
        simulatedAnnealing.run();
    }
}
