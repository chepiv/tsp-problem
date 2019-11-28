package com.chepiv.algorithm;

import com.google.common.collect.Lists;
import com.chepiv.model.City;
import com.chepiv.model.Genome;
import com.chepiv.utils.*;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by chepiv on 21/10/2019.
 * Contact: chepurin.ivan@gmail.com
 * Github:chepiv
 */
public class GeneticAlgorithmRunner implements Algorithm{

    private GeneticAlgorithm algorithm;
    private int populationSize = 100;
    private int generations = 100;
    private int startingCity = 0;
    private double px = 0.9;
    private double pm = 0.1;
    int tournamentSize = 10;
    private MutationType mutationType = MutationType.INVERSE;
    private CrossoverType crossoverType = CrossoverType.PMX;
    private CsvWriter csvWriter = new CsvWriter();
    String berlin52 = "berlin52";
    String gr666 = "gr666";
    String kroA100 = "kroA100";
    private String file = berlin52;
    int identifierOfTheRun;
    Map<String,Integer> fileToFitness;
    int bestFitness;
    int iterations;
    int worstFitness;


    public GeneticAlgorithmRunner(int populationSize,
                                  int generations,
                                  double px,
                                  double pm,
                                  int tournamentSize,
                                  MutationType mutationType,
                                  CrossoverType crossoverType,
                                  String file,
                                  int identifierOfTheRun,
                                  Map<String,Integer> fileToFitness) {
        this.populationSize = populationSize;
        this.generations = generations;
        this.px = px;
        this.pm = pm;
        this.tournamentSize = tournamentSize;
        this.mutationType = mutationType;
        this.crossoverType = crossoverType;
        this.file = file;
        this.identifierOfTheRun = identifierOfTheRun;
        this.fileToFitness = fileToFitness;
    }

    public void run() throws Exception {

        List<City> cities = DataParser.loadData(file);

        DistanceMatrix distanceMatrix = new DistanceMatrix(cities);
        distanceMatrix.calculateMatrix();

        MatrixSingleton matrixSingleton = MatrixSingleton.getInstance();

        //GENERATING RANDOM FIRST POPULATION

        double temperature = 1500;
        double coolingRate = 0.9998;
        int startingCity = 0;
        String fileName = "data/kroA100.tsp";
        int numOfNeighbours = 5;
        int maxNumOfIterations = 3000;
        List<Double> best = new ArrayList<>();



        List<Genome> population = Lists.newArrayList();
        int numberOfCities = cities.size();
        for (int i = 0; i < populationSize; i++) {
//            population.add(new Genome(startingCity, numberOfCities));
            SimulatedAnnealing simulatedAnnealing = new SimulatedAnnealing(file,temperature,maxNumOfIterations,numOfNeighbours,coolingRate);
            simulatedAnnealing.run();
            population.add(simulatedAnnealing.getBestGenome());
        }



        int reproductionSize = 2;

        int genomeSize = population.get(0).getRoute().size();
        algorithm = new GeneticAlgorithm(genomeSize, numberOfCities, reproductionSize, startingCity, tournamentSize);

        List<Integer> generationsX = Lists.newArrayList();
        List<Integer> bestIndividuals = Lists.newArrayList();
        List<Double> averageIndividuals = Lists.newArrayList();
        List<Integer> worstIndividuals = Lists.newArrayList();
        List<CsvResultLine> csvResults = Lists.newArrayList();

        for (int i = 0; i < generations; i++) {
            List<Genome> selectedAndCrossedPopulation = fillPopulation(population);
            mutatePopulation(selectedAndCrossedPopulation, mutationType);
            Genome bestGenome = Collections.min(selectedAndCrossedPopulation);
            Genome worthGenome = Collections.max(selectedAndCrossedPopulation);
            OptionalDouble averageGenome = selectedAndCrossedPopulation
                    .stream()
                    .mapToDouble(Genome::getFitness)
                    .average();
            Double average = averageGenome.isPresent() ? averageGenome.getAsDouble() : null;
            System.out.println("BEST GENOME");
            System.out.println(bestGenome);

            generationsX.add(i);
            bestIndividuals.add(bestGenome.getFitness());
            averageIndividuals.add(average);
            worstIndividuals.add(worthGenome.getFitness());

            csvResults.add(new CsvResultLine(i, worthGenome.getFitness(), average, bestGenome.getFitness()));

            population = selectedAndCrossedPopulation;
        }

        String filename = "results/" + generations + "/" + populationSize + "/" + identifierOfTheRun + "_" + file.substring(file.lastIndexOf("/") + 1) + ".csv";

//        updateFileToFitnessMap(filename,csvResults.get(csvResults.size() -1).getBestResult(),csvResults);
//        saveResultToFile(csvResults,filename);

//        System.out.println("--------------------------------------FINAL BEST RESULTS TO ANALYZE--------------------------------------");
        Integer min = Collections.min(bestIndividuals);
        Integer max = Collections.max(bestIndividuals);
        System.out.println("OPTIMUM: "+ min);
        bestFitness = min;
        worstFitness = max;
//        System.out.println("Number of iterations" + populationSize*generations);
        iterations = populationSize*generations;
        drawChart(null,bestIndividuals,averageIndividuals,worstIndividuals);


    }


    public Pair<Integer, Integer> getDataToAnalytics(){
        return Pair.of(bestFitness,worstFitness);
    }

    private void saveResultToFile(List<CsvResultLine> csvResults, String filename) throws Exception {
        csvWriter.writeCsvFromBean(Paths.get(filename), csvResults);
    }

    private void updateFileToFitnessMap(String filename,Integer fitness, List<CsvResultLine> csvResults) throws Exception {
        if (fileToFitness.size() < 5){
            fileToFitness.put(filename,fitness);
            saveResultToFile(csvResults,filename);
        }else {
            Map.Entry<String, Integer> max = Collections.max(fileToFitness.entrySet(), Comparator.comparingInt(Map.Entry::getValue));
            if (fitness<max.getValue()) {
                boolean delete = new File(max.getKey()).delete();
                fileToFitness.remove(max.getKey());
                saveResultToFile(csvResults,filename);
                fileToFitness.put(filename,fitness);
            }
        }
    }

    private List<Genome> fillPopulation(List<Genome> population) {
        System.out.println("SELECTION PROCESS HAS STARTED");
        List<Genome> newPopulation = new ArrayList<>();
        while (!isPopulationFilled(newPopulation)) {
            List<Genome> parents = algorithm.selection(population);

            double rand = ThreadLocalRandom.current().nextDouble(0, 1);
            if (rand <= px) {
                System.out.println("CROSSOVER PROCESS HAS STARTED");
                List<Genome> kids = crossover(parents, crossoverType);
                kids.forEach(System.out::println);
                newPopulation.addAll(kids);
            } else {
                newPopulation.addAll(parents);
            }
        }
        return new ArrayList<>(newPopulation);
    }

    private List<Genome> crossover(List<Genome> population, CrossoverType crossoverType) {
        if (crossoverType == CrossoverType.OX) {
            return algorithm.orderedCrossover(population);
        } else return algorithm.pmxCrossover(population);
    }

    private void mutatePopulation(List<Genome> population, MutationType mutationType) {
        System.out.println("MUTATION PROCESS HAS STARTED");
        if (mutationType == MutationType.SWAP) {
            algorithm.swap(population, pm);
        } else algorithm.inverse(population, pm);
    }

    private boolean isPopulationFilled(List<Genome> population) {
        return population.size() == populationSize;
    }


    private String configDescription() {
        return toString();
    }

    @Override
    public String toString() {
        return
                " populationSize=" + populationSize +
                        ", generations=" + generations +
                        ", px=" + px +
                        ", pm=" + pm +
                        ", tournamentSize=" + tournamentSize +
                        ", mutationType=" + mutationType +
                        ", crossoverType=" + crossoverType +
                        ", file='" + file + '\'';
    }
}
