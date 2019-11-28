package com.chepiv;

import com.chepiv.algorithm.CrossoverType;
import com.chepiv.algorithm.GeneticAlgorithm;
import com.chepiv.algorithm.GeneticAlgorithmRunner;
import com.chepiv.algorithm.MutationType;
import com.chepiv.utils.CsvWriter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by chepiv on 11/10/2019.
 * Contact: chepurin.ivan@gmail.com
 * Github:chepiv
 */
public class Main {

    static List<Pair<Integer,Integer>> pairs = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        GeneticAlgorithm algorithm;
        int[] generations = {1000};
        int[] populationSizes = {1000};
        double[] pxRates = {0.9};
        double[] pmRates = {0.05};
        int startingCity = 0;
        int tournamentSize = 0;
//        MutationType[] mutationTypes = {MutationType.SWAP, MutationType.INVERSE};
        MutationType[] mutationTypes = {MutationType.INVERSE};
//        CrossoverType[] crossoverTypes = {CrossoverType.PMX, CrossoverType.OX};
        CrossoverType[] crossoverTypes = {CrossoverType.PMX};
        CsvWriter csvWriter = new CsvWriter();
        String berlin52 = "berlin52";
        String gr666 = "gr666";
        String kroA100 = "kroA100";
//        String file = "data/berlin52.tsp";

        Map<String,Integer> fileToFitness = new HashMap<>(5);


        String[] filesNamesEasy = {"data/ali535.tsp"};
//        String[] filesNamesMedium = {"data/kroA200.tsp"}; //remove 100, 150 for 500


        for (int z = 0; z < 2; z++) {
            for (String fileName : filesNamesEasy) {
                int i = 0;
                for (int generation : generations) {
                    for (int populationSize : populationSizes) {
                        for (double pm : pmRates) {
                            for (double px : pxRates) {
                                for (CrossoverType crossoverType : crossoverTypes) {
                                    for (MutationType mutationType : mutationTypes) {
                                        tournamentSize =  (populationSize *10) /100;
                                        GeneticAlgorithmRunner algorithmRunner = new GeneticAlgorithmRunner(populationSize, generation, px, pm, tournamentSize, mutationType, crossoverType, fileName,i,fileToFitness);
                                        algorithmRunner.run();
                                        Pair<Integer, Integer> dataToAnalytics = algorithmRunner.getDataToAnalytics();
                                        pairs.add(dataToAnalytics);
                                        String configuration = i + algorithmRunner.toString() + System.lineSeparator();
                                        i++;
                                        FileUtils.write(new File("results/"+fileName+"_config.txt"),configuration,true);
                                    }
                                }
                            }
                        }
                        fileToFitness.clear();
                    }
                }
            }
            printResults();
        }


    }

    private static void printResults() {
        StandardDeviation sd = new StandardDeviation();
        double[] doubles = pairs.stream().map(Pair::getLeft).mapToDouble(Integer::doubleValue).toArray();
        List<Integer> fitnesses = pairs.stream().map(Pair::getLeft).collect(Collectors.toList());
        double[] doublesWorst = pairs.stream().map(Pair::getRight).mapToDouble(Integer::doubleValue).toArray();
        List<Integer> fitnessesWorst = pairs.stream().map(Pair::getRight).collect(Collectors.toList());
        double standardDeviation = sd.evaluate(doubles);
        System.out.println("---------------------------VERY FINAL FROM 10 times-------------------");
        System.out.println("BEST GENOME: " + Collections.min(fitnesses));
        fitnesses.stream().forEach(System.out::println);
        System.out.println("AVERAGE GENOME:" + fitnesses.stream().mapToDouble(Integer::doubleValue).average().getAsDouble());
        System.out.println("WORST GENOME: " + Collections.max(fitnessesWorst));
        System.out.println("ITERATIONS:" + pairs.stream().map(Pair::getRight).findFirst());
        System.out.println("STANDARD DEVIATION:" + standardDeviation);
    }
}
