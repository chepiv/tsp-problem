package com.chepiv;

import com.chepiv.model.City;
import com.chepiv.model.Genome;
import com.chepiv.utils.DataParser;
import com.chepiv.utils.DistanceMatrix;
import com.chepiv.utils.MatrixSingleton;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by chepiv on 22/11/2019.
 * Contact: chepurin.ivan@gmail.com
 * Github:chepiv
 */
public class GreedyMain {

    public static void main(String[] args) throws IOException {
        String file = "data/ali535.tsp";
        List<City> cities = DataParser.loadData(file);

        DistanceMatrix distanceMatrix = new DistanceMatrix(cities);
        distanceMatrix.calculateMatrix();

        MatrixSingleton matrixSingleton = MatrixSingleton.getInstance();
        List<Double> best = new ArrayList<>();
        List<Double> worst = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            createGreedy(cities, matrixSingleton, best);
        }

        StandardDeviation sd = new StandardDeviation();
        double evaluate = sd.evaluate(best.stream().mapToDouble(Double::doubleValue).toArray());
        System.out.println("BEST FITNESS: " + Collections.min(best));
        System.out.println("WORST FITNESS: " + Collections.max(best));
        best.stream().forEach(System.out::println);
        System.out.println("AVERAGE:" + best.stream().mapToDouble(Double::doubleValue).average());
        System.out.println("DIVIATION: " + evaluate);

    }

    private static void createGreedy(List<City> cities, MatrixSingleton matrixSingleton, List<Double> best) {
        List<Integer> resultRoute = new ArrayList<>();
        Multimap<Integer,Double> multiMap = ArrayListMultimap.create();

        for (int i = 0; i < cities.size(); i++) {
            for (int j = 0; j < cities.size(); j++) {
                multiMap.put(i, matrixSingleton.getDistances()[i][j]);
            }
        }

        List<Double> doubles = (List<Double>) multiMap.get(0);
        doubles.set(0,null);
        while (resultRoute.size() != cities.size() - 1) {
            int index = getNextIndex(doubles,resultRoute);
            if (index != 0 && !resultRoute.contains(index)) {
                resultRoute.add(index);
                doubles = (List<Double>) multiMap.get(resultRoute.get(resultRoute.size()-1));
            }
        }

        Genome genome = new Genome(resultRoute, 0, cities.size());
        int fitness = genome.getFitness();
        best.add((double) fitness);
        System.out.println(genome);
    }

    private static int getNextIndex(List<Double> doubles, List<Integer> currentRoute) {
        int nextIndex = doubles.indexOf(getMin(doubles));
        if (currentRoute.contains(nextIndex) || nextIndex == 0) {
            doubles.set(nextIndex,null);
            doubles.set(0,null);
            nextIndex = doubles.indexOf(getMin(doubles));
        }
        return nextIndex;
    }

    private static Double getMin(List<Double> doubles) {
        Double min = Double.MAX_VALUE;
        for (Double aDouble : doubles) {
            if (aDouble != null && aDouble<min) {
                min = aDouble;
            }
        }
        return min;
    }
}
