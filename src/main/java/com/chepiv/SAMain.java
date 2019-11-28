package com.chepiv;

import com.chepiv.algorithm.SimulatedAnnealing;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by chepiv on 30/10/2019.
 * Contact: chepurin.ivan@gmail.com
 * Github:chepiv
 */
public class SAMain {
    public static void main(String[] args) throws Exception {
        double temperature = 1500;
        double coolingRate = 0.9998;
        int startingCity = 0;
        String fileName = "data/kroA200.tsp";
        int numOfNeighbours = 5;
        int maxNumOfIterations = 3000;
        List<Double> best = new ArrayList<>();



        for (int i = 0; i < 10; i++) {
            SimulatedAnnealing simulatedAnnealing = new SimulatedAnnealing(fileName,temperature, maxNumOfIterations, numOfNeighbours, coolingRate);
            simulatedAnnealing.run();
            double dataForAnalytics = simulatedAnnealing.getDataForAnalytics();
            best.add(dataForAnalytics);
        }

        StandardDeviation sd = new StandardDeviation();
        double evaluate = sd.evaluate(best.stream().mapToDouble(Double::doubleValue).toArray());
        System.out.println("BEST FITNESS: " + Collections.min(best));
        System.out.println("WORST FITNESS: " + Collections.max(best));
        best.stream().forEach(System.out::println);
        System.out.println("AVERAGE:" + best.stream().mapToDouble(Double::doubleValue).average());
        System.out.println("DIVIATION: " + evaluate);
    }
}
