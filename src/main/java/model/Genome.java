package model;

import utils.MatrixSingleton;

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
        this.route = route;
        this.fitness = calculateFitness();
    }

    public Genome(int startingCity, int numberOfCities) {
        this.numberOfCities = numberOfCities;
        this.startingCity = startingCity;
        distances = MatrixSingleton.getInstance().getDistances();
        this.route = randomSalesman();
        this.fitness = calculateFitness();
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

    public int calculateFitness() {
        int fitness = 0;
        int currentCity = startingCity;

        for (int gene : route) {
            fitness += distances[currentCity][gene];
            currentCity = gene;
        }


        fitness += distances[route.get(numberOfCities-2)][startingCity];

        return fitness;
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
