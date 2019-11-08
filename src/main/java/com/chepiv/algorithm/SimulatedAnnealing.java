package com.chepiv.algorithm;

import com.chepiv.model.City;
import com.chepiv.model.Genome;
import com.chepiv.utils.CsvResultLine;
import com.chepiv.utils.CsvWriter;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.style.markers.SeriesMarkers;

import java.io.IOException;
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
        System.out.println("Final solution distance: " + bestIndividual.getFitness());
        System.out.println("Tour: " + bestIndividual);


        drawChart(candidateHistory, bestFitnessesHistory, generationsHistory);
        draw();
    }


    //        drawPlot(bestFitnessesHistory, generationsHistory);
//        drawPlot(bestCandidateHistory, generationsHistory);




    private void drawChart(List<Integer> candidates, List<Integer> bestInds, List<Integer> generations) {
        try {
            double[] xData = new double[]{0.0, 1.0, 2.0};
            double[] yData = new double[]{2.0, 1.0, 0.0};

// Create Chart
            XYChart chart = new XYChartBuilder().width(600).height(400).title("Area Chart").xAxisTitle("X").yAxisTitle("Y").build();
            chart.addSeries("candidates",candidates).setMarker(SeriesMarkers.NONE);
            chart.addSeries("bestInds",bestInds).setMarker(SeriesMarkers.NONE);
// Show it
            new SwingWrapper(chart).displayChart();

// Save it
            BitmapEncoder.saveBitmap(chart, "./Sample_Chart", BitmapEncoder.BitmapFormat.PNG);

// or save it in high-res
            BitmapEncoder.saveBitmapWithDPI(chart, "./Sample_Chart_300_DPI", BitmapEncoder.BitmapFormat.PNG, 300);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
