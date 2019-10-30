package com.chepiv.algorithm;

import com.chepiv.model.City;
import com.chepiv.model.Genome;
import com.chepiv.model.TabuList;
import com.chepiv.utils.DataParser;
import com.chepiv.utils.DistanceMatrix;

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
                      TabuList tabuList, int numOfNeighbours,
                      int maxIterations, String fileName, int startingCity) {
        this.tabuListSize = tabuListSize;
        this.tabuList = tabuList;
        this.numOfNeighbours = numOfNeighbours;
        this.maxIterations = maxIterations;
        this.fileName = fileName;
        this.startingCity = startingCity;
    }

    public void run() throws IOException {
        List<City> cities = DataParser.loadData(fileName);
        int numberOfCities = cities.size();
        DistanceMatrix distanceMatrix = new DistanceMatrix(cities);
        distanceMatrix.calculateMatrix();

        int currentMaxIterations = 0;
        while (currentMaxIterations < maxIterations) {
            Genome individual = getRandomIndividual(cities);
            tabuList.add(individual);
            List<Genome> neighbours = getNeighbours(individual, numberOfCities);
            Genome candidate = getBestNeighbour(neighbours);

            if (candidate.getFitness() < individual.getFitness()) {
                tabuList.add(candidate);
                currentMaxIterations = 0;
            }

        }



    }

    private Genome getRandomIndividual(List<City> cities) {
        return new Genome(startingCity,cities.size());
    }

    private List<Genome> getNeighbours(Genome genome, int numberOfCities){
        List<Genome> neighbours = new ArrayList<>();
        for (int i = 0; i < numOfNeighbours; i++) {
            neighbours.add(getNeighbour(genome,numberOfCities));
        }
        return neighbours;
    }

    private Genome getNeighbour(Genome genome, int numberOfCities) {
        Random random = new Random();
        List<Integer> route = new ArrayList<>();
        Collections.copy(route, genome.getRoute());
        Collections.swap(route, random.nextInt(route.size()-1),random.nextInt(route.size()-1));
        return new Genome(route,startingCity,numberOfCities);
    }

    private Genome getBestNeighbour(List<Genome> neighbours){
        return Collections.min(neighbours);
    }
}
