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
        List<Integer> generationsHistory = new ArrayList<>();
        List<Integer> bestCandidateHistory = new ArrayList<>();
        List<Integer> worstNeighboursHistory = new ArrayList<>();
        int i = 0;
        List<City> cities = getCities(filename);
        int numberOfCities = cities.size();
        Genome bestIndividual = getRandomIndividual(startingCity, cities);
        Genome candidate = new Genome(bestIndividual.getRoute(), startingCity, numberOfCities);

        while (temperature > 1 && i<maxNumOfIterations) {
            List<Genome> neighbours = getNeighbours(candidate, numberOfCities);
            Genome neighbour = getBestNeighbour(neighbours);
            Genome worstNeighbour = getWorstNeighbour(neighbours);

            int currentBestFitness = bestIndividual.getFitness();
            int neighbourFitness = neighbour.getFitness();
            bestCandidateHistory.add(neighbourFitness);
            worstNeighboursHistory.add(worstNeighbour.getFitness());

            if (isSolutionAccepted(currentBestFitness, neighbourFitness)) {
                candidate = new Genome(neighbour.getRoute(), startingCity, numberOfCities);
            }

            if (candidate.getFitness() < bestIndividual.getFitness()) {
                bestIndividual = new Genome(candidate.getRoute(), startingCity, numberOfCities);
            }


            temperature = temperature * coolingRate; // temp goes to fast
            generationsHistory.add(i++);
            bestFitnessesHistory.add(bestIndividual.getFitness());
            System.out.println("Final solution distance: " + bestIndividual.getFitness());
            System.out.println("Tour: " + bestIndividual);
            csvResults.add(new CsvResultLine(i, worstNeighbour.getFitness(), (double) candidate.getFitness(), currentBestFitness, temperature));
        }
        draw();

//        drawPlot(bestFitnessesHistory,generationsHistory);
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
        double exp = Math.exp((currentBestFitness - candidateFitness) / temperature);
        double ap = Math.pow(Math.E,
                (currentBestFitness - candidateFitness)/temperature);
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
