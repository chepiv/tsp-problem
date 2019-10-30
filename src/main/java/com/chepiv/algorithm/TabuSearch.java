package com.chepiv.algorithm;

import com.chepiv.model.City;
import com.chepiv.model.Genome;
import com.chepiv.model.TabuList;
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
public class TabuSearch {
    private int tabuListSize;
    private TabuList tabuList;
    private int numOfNeighbours;
    private int maxIterations;
    private String fileName;
    private int startingCity;

    public TabuSearch(int tabuListSize,
                      int numOfNeighbours,
                      int maxIterations, String fileName, int startingCity) {
        this.tabuListSize = tabuListSize;
        this.tabuList = new TabuList(tabuListSize);
        this.numOfNeighbours = numOfNeighbours;
        this.maxIterations = maxIterations;
        this.fileName = fileName;
        this.startingCity = startingCity;
    }

    public void run() throws IOException, PythonExecutionException {
        List<City> cities = DataParser.loadData(fileName);
        int numberOfCities = cities.size();
        DistanceMatrix distanceMatrix = new DistanceMatrix(cities);
        distanceMatrix.calculateMatrix();
        List<Integer> bestFitnessesHistory = new ArrayList<>();
        List<Integer> generationsHistory = new ArrayList<>();
        int i = 0;

        int currentMaxIterations = 0;
        Genome best = getRandomIndividual(cities);
        while (currentMaxIterations < maxIterations) {
            tabuList.add(best);
            List<Genome> neighbours = getNeighbours(best, numberOfCities);
            Genome candidate = getBestNeighbour(neighbours);

            if (candidate.getFitness() < best.getFitness() && !tabuList.contains(candidate)) {
                System.out.println("FOUND NEW BEST: " + best);
                tabuList.add(candidate);
                best = candidate;
                currentMaxIterations = 0;
                bestFitnessesHistory.add(best.getFitness());
                generationsHistory.add(i++);
            } else {
                currentMaxIterations++;
            }
        }
        Plot plt = Plot.create();
        plt.plot()
                .add(generationsHistory, bestFitnessesHistory)
                .linestyle("--");

        plt.show();
    }

    private Genome getRandomIndividual(List<City> cities) {
        return new Genome(startingCity, cities.size());
    }

    private List<Genome> getNeighbours(Genome genome, int numberOfCities) {
        List<Genome> neighbours = new ArrayList<>();
        for (int i = 0; i < numOfNeighbours; i++) {
            neighbours.add(getNeighbour(genome, numberOfCities));
        }
        return neighbours;
    }

    private Genome getNeighbour(Genome genome, int numberOfCities) {
        Random random = new Random();
        List<Integer> route = new ArrayList<>(genome.getRoute());
        Collections.swap(route, random.nextInt(route.size() - 1), random.nextInt(route.size() - 1));
        return new Genome(route, startingCity, numberOfCities);
    }

    private Genome getBestNeighbour(List<Genome> neighbours) {
        return Collections.min(neighbours);
    }
}
