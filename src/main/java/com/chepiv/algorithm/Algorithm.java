package com.chepiv.algorithm;

import com.chepiv.model.City;
import com.chepiv.model.Genome;
import com.chepiv.utils.DataParser;
import com.chepiv.utils.DistanceMatrix;
import com.github.sh0nk.matplotlib4j.Plot;
import com.github.sh0nk.matplotlib4j.PythonExecutionException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by chepiv on 30/10/2019.
 * Contact: chepurin.ivan@gmail.com
 * Github:chepiv
 */
public interface Algorithm {

    int startingCity = 0;

    void run() throws IOException, PythonExecutionException;

    default Genome getRandomIndividual(int startingCity, List<City> cities) {
        return new Genome(startingCity, cities.size());
    }

    default List<City> getCities(String filename) throws IOException {
        List<City> cities = DataParser.loadData(filename);
        int numberOfCities = cities.size();
        DistanceMatrix distanceMatrix = new DistanceMatrix(cities);
        distanceMatrix.calculateMatrix();
        return cities;
    }

    default Genome getNeighbour(Genome genome, int numberOfCities) {
        Random random = new Random();
        List<Integer> route = new ArrayList<>(genome.getRoute());
        Collections.swap(route, random.nextInt(route.size() - 1), random.nextInt(route.size() - 1));
        return new Genome(route, startingCity, numberOfCities);
    }

    default void drawPlot(List<Integer> bestFitnessesHistory, List<Integer> generationsHistory) throws IOException, PythonExecutionException {
        Plot plt = Plot.create();
        plt.plot()
                .add(generationsHistory, bestFitnessesHistory)
                .linestyle("--");

        plt.show();
    }
}
