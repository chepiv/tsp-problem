package com.chepiv;

import com.chepiv.algorithm.SimulatedAnnealing;

/**
 * Created by chepiv on 30/10/2019.
 * Contact: chepurin.ivan@gmail.com
 * Github:chepiv
 */
public class SAMain {
    public static void main(String[] args) throws Exception {
        double temperature = 1500;
        double coolingRate = 0.9995;
        int startingCity = 0;
        String fileName = "data/berlin52.tsp";
        int numOfNeighbours = 5;
        int maxNumOfIterations = 3000;
        SimulatedAnnealing simulatedAnnealing = new SimulatedAnnealing(fileName,temperature, maxNumOfIterations, numOfNeighbours, coolingRate);
        simulatedAnnealing.run();
    }
}
