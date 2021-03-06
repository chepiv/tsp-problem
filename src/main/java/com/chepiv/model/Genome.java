package com.chepiv.model;

import com.chepiv.utils.MatrixSingleton;
import com.google.common.annotations.VisibleForTesting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by chepiv on 12/10/2019.
 * Contact: chepurin.ivan@gmail.com
 * Github:chepiv
 */
public class Genome implements Comparable<Genome>{

    private List<Integer> route;

    private int startingCity;
    private int numberOfCities;
    private int fitness;

    private double [][] distances;


    public Genome(List<Integer> route, int startingCity, int numberOfCities) {
        this.numberOfCities = numberOfCities;
        this.startingCity = startingCity;
        distances = MatrixSingleton.getInstance().getDistances();
//        this.route = route.stream().map(Integer::new).collect(Collectors.toList());
        this.route = deepCopy(route);
        this.fitness = calculateFitness();
    }

    private List<Integer> deepCopy(List<Integer> route) {
        List<Integer> result = new ArrayList<>();
        for (Integer integer : route) {
            result.add(new Integer(integer));
        }
        return result;
    }

    public Genome(int startingCity, int numberOfCities) {
        this.numberOfCities = numberOfCities;
        this.startingCity = startingCity;
        distances = MatrixSingleton.getInstance().getDistances();
        this.route = randomSalesman();
        this.fitness = calculateFitness();
    }

    @VisibleForTesting
    Genome(List<Integer> route, int startingCity, int numberOfCities, double[][] distances) {
        this.route = route;
        this.startingCity = startingCity;
        this.numberOfCities = numberOfCities;
        this.distances = distances;
    }

    private List<Integer> randomSalesman() {
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < numberOfCities; i++) {
            if (i != startingCity)
                result.add(i);
        }
        Collections.shuffle(result);
        return result;
    }

    private int calculateFitness() {
        int fitness = 0;
        int currentCity = startingCity;

        for (int gene : route) {
            fitness += distances[currentCity][gene];
            currentCity = gene;
        }


        fitness += distances[route.get(numberOfCities-2)][startingCity];

        return fitness;
    }

    public void recalculateFitness() {
        this.fitness = calculateFitness();
    }

    public int getFitness() {
        return fitness;
    }

    public List<Integer> getRoute() {
        return route;
    }

    @Override
    public String toString() {
        return "Genome{" +
                "route=" + route +
                ", startingCity=" + startingCity +
                ", numberOfCities=" + numberOfCities +
                ", fitness=" + fitness +
                '}';
    }

    @Override
    public int compareTo(Genome o) {
        return Integer.compare(this.fitness, o.fitness);
    }
}
