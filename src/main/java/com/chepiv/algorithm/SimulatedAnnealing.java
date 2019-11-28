package com.chepiv.algorithm;

import com.chepiv.model.City;
import com.chepiv.model.Genome;
import com.chepiv.utils.CsvResultLine;
import com.chepiv.utils.CsvWriter;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
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
    private int numOfNeighbours;
    private double coolingRate;
    private List<CsvResultLine> csvResults;
    private CsvWriter csvWriter;
    private double bestFitness;

    public SimulatedAnnealing(String filename, double temperature, int maxNumOfIterations, int numOfNeighbours, double coolingRate) {
        this.filename = filename;
        this.temperature = temperature;
        this.maxNumOfIterations = maxNumOfIterations;
        this.numOfNeighbours = numOfNeighbours;
        this.coolingRate = coolingRate;
        csvResults = new ArrayList<>();
        csvWriter = new CsvWriter();
    }

    @Override
    public void run() throws Exception {
        List<Integer> bestFitnessesHistory = new ArrayList<>();
        List<Integer> candidateHistory = new ArrayList<>();
        List<Integer> generationsHistory = new ArrayList<>();
        int i = 0;
        List<City> cities = getCities(filename);
        int numberOfCities = cities.size();
        Genome candidate = getRandomIndividual(startingCity, cities);
        Genome bestIndividual = new Genome(candidate.getRoute(), startingCity, numberOfCities);

        while (temperature > 1) {
            Genome neighbour = getNeighbour(candidate, numberOfCities);
//            Genome neighbour = getBestNeighbour(getNeighbours(candidate,numberOfCities));
            int currentBestFitness = candidate.getFitness();
            int neighbourFitness = neighbour.getFitness();
            if (isSolutionAccepted(currentBestFitness, neighbourFitness)) {
                candidate = new Genome(neighbour.getRoute(), startingCity, numberOfCities);
            }
            if (candidate.getFitness() < bestIndividual.getFitness()) {
                bestIndividual = candidate; // try to use new
            }
            temperature *= coolingRate;
            generationsHistory.add(i++);
            candidateHistory.add(candidate.getFitness());
            bestFitnessesHistory.add(bestIndividual.getFitness());
            System.out.println("Final solution distance: " + bestIndividual.getFitness());
            System.out.println("Tour: " + bestIndividual);
            csvResults.add(new CsvResultLine(i, 0, (double) candidate.getFitness(), bestIndividual.getFitness(), temperature));
        }


        generationsHistory.add(i++);
        bestFitnessesHistory.add(bestIndividual.getFitness());
        int fitness = bestIndividual.getFitness();
        bestFitness = fitness;
        System.out.println("Final solution distance: " + fitness);
        System.out.println("Tour: " + bestIndividual);


//        drawChart(candidateHistory, bestFitnessesHistory, null, null);
//        draw();
    }

    public double getDataForAnalytics(){
        return bestFitness;
    }


    //        drawPlot(bestFitnessesHistory, generationsHistory);
//        drawPlot(bestCandidateHistory, generationsHistory);


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
        double exp = Math.exp((currentBestFitness - candidateFitness) / temperature);
        double ap = Math.pow(Math.E,
                (currentBestFitness - candidateFitness) / temperature);
        return exp;
    }

    private List<Genome> getNeighbours(Genome genome, int numberOfCities) {
        List<Genome> neighbours = new ArrayList<>();
        for (int i = 0; i < numOfNeighbours; i++) {
            neighbours.add(getNeighbour(genome, numberOfCities));
        }
        return neighbours;
    }

    private Genome getBestNeighbour(List<Genome> neighbours) {
        return Collections.min(neighbours);
    }

    private Genome getWorstNeighbour(List<Genome> neighbours) {
        return Collections.max(neighbours);
    }

    private void draw() throws Exception {
        saveResultToFile(csvResults, "resutls/SA_berlin_123.csv");
    }

    private void saveResultToFile(List<CsvResultLine> csvResults, String filename) throws Exception {
        csvWriter.writeCsvFromBean(Paths.get(filename), csvResults);
    }
}
