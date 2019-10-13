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
public class Genome {

    List<Integer> route;

    int startingCity;
    int numberOfCities;
    int fitness;
    double [][] distances;


    public Genome(List<Integer> route, int startingCity, int numberOfCities, int fitness) {
        this.route = route;
        this.startingCity = startingCity;
        this.numberOfCities = numberOfCities;
        this.fitness = fitness;
        distances = MatrixSingleton.getInstance().getDistances();
    }

    public Genome(int startingCity, int numberOfCities, int fitness) {
        this.route = randomSalesman();
        this.startingCity = startingCity;
        this.numberOfCities = numberOfCities;
        this.fitness = fitness;
        distances = MatrixSingleton.getInstance().getDistances();
    }

    private List<Integer> randomSalesman() {
        List<Integer> result = new ArrayList<Integer>();
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

        // Calculating path cost
        for (int gene : route) {
            fitness += distances[currentCity][gene];
            currentCity = gene;
        }

        // We have to add going back to the starting city to complete the circle
        // the genome is missing the starting city, and indexing starts at 0, which is why we subtract 2
        fitness += distances[route.get(numberOfCities-2)][startingCity];

        return fitness;
    }
}
