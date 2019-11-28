package com.chepiv;

import com.chepiv.algorithm.TabuSearchAlgorithm;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by chepiv on 30/10/2019.
 * Contact: chepurin.ivan@gmail.com
 * Github:chepiv
 */
public class TabuSearchMain {
    public static void main(String[] args) throws Exception {
        int tabuListSize = 10;
        int numOfNeighbours = 20;
        int maxIterations = 2000;
        String fileName = "data/kroA200.tsp";
        int startingCity = 0;
        List<Double> bestFitnesses = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            TabuSearchAlgorithm tabuSearchAlgorithm = new TabuSearchAlgorithm(tabuListSize, numOfNeighbours, maxIterations, fileName, startingCity);
            tabuSearchAlgorithm.run();
            bestFitnesses.add(tabuSearchAlgorithm.getDataForAnalytics());
        }
        StandardDeviation sd = new StandardDeviation();
        double evaluate = sd.evaluate(bestFitnesses.stream().mapToDouble(Double::doubleValue).toArray());
        System.out.println("BEST FITNESS: " + Collections.min(bestFitnesses));
        System.out.println("WORST FITNESS: " + Collections.max(bestFitnesses));
        bestFitnesses.stream().forEach(System.out::println);
        System.out.println("AVERAGE:" + bestFitnesses.stream().mapToDouble(Double::doubleValue).average());
        System.out.println("DIVIATION: " + evaluate);

    }
}
