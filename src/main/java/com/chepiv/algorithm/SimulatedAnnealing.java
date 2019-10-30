package com.chepiv.algorithm;

import com.chepiv.model.City;
import com.chepiv.model.Genome;
import com.github.sh0nk.matplotlib4j.PythonExecutionException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by chepiv on 30/10/2019.
 * Contact: chepurin.ivan@gmail.com
 * Github:chepiv
 */
public class SimulatedAnnealing implements Algorithm {

    private String filename;
    private double temperature;
    private int maxNumOfIterations;
    private double coolingRate;

    public SimulatedAnnealing(String filename, double temperature, int maxNumOfIterations, double coolingRate) {
        this.filename = filename;
        this.temperature = temperature;
        this.maxNumOfIterations = maxNumOfIterations;
        this.coolingRate = coolingRate;
    }

    @Override
    public void run() throws IOException, PythonExecutionException {
        List<Integer> bestFitnessesHistory = new ArrayList<>();
        List<Integer> generationsHistory = new ArrayList<>();
        int i = 0;
        List<City> cities = getCities(filename);
        int numberOfCities = cities.size();
        Genome bestIndividual = getRandomIndividual(startingCity, cities);

        while (temperature > 1) {
            Genome neighbour = getNeighbour(bestIndividual, numberOfCities);
            int currentBestFitness = bestIndividual.getFitness();
            int neighbourFitness = neighbour.getFitness();
            if (isSolutionAccepted(currentBestFitness, neighbourFitness)) {
                bestIndividual = new Genome(neighbour.getRoute(),startingCity,numberOfCities);
            }
            temperature *= 1 - coolingRate;
            generationsHistory.add(i++);
            bestFitnessesHistory.add(bestIndividual.getFitness());
            System.out.println("Final solution distance: " + bestIndividual.getFitness());
            System.out.println("Tour: " + bestIndividual);
        }

        drawPlot(bestFitnessesHistory,generationsHistory);
    }

    private boolean isSolutionAccepted(int currentBestFitness, int candidateFitness) {
        double rand = ThreadLocalRandom.current().nextDouble(0, 1);
        if (rand < acceptanceProbability(currentBestFitness, candidateFitness, temperature)) {
            return true;
        } else {
            return false;
        }
    }

    private static double acceptanceProbability(int currentBestFitness, int candidateFitness, double temperature) {
        if (candidateFitness < currentBestFitness) {
            return 1.0;
        }
        return Math.exp((currentBestFitness - candidateFitness) / temperature);
    }
}
