package com.chepiv;

import com.chepiv.algorithm.GeneticAlgorithmRunner;
import com.chepiv.algorithm.CrossoverType;
import com.chepiv.algorithm.MutationType;
import com.chepiv.algorithm.GeneticAlgorithm;
import org.apache.commons.io.FileUtils;
import com.chepiv.utils.CsvWriter;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chepiv on 11/10/2019.
 * Contact: chepurin.ivan@gmail.com
 * Github:chepiv
 */
public class Main {
    public static void main(String[] args) throws Exception {
        GeneticAlgorithm algorithm;
        int[] generations = {1000};
        int[] populationSizes = {100,1000};
        double[] pxRates = {0.5, 1.0};
        double[] pmRates = {0.1, 0.35};
        int startingCity = 0;
        int tournamentSize = 0;
        MutationType[] mutationTypes = {MutationType.SWAP, MutationType.INVERSE};
        CrossoverType[] crossoverTypes = {CrossoverType.PMX, CrossoverType.OX};
        CsvWriter csvWriter = new CsvWriter();
        String berlin52 = "berlin52";
        String gr666 = "gr666";
        String kroA100 = "kroA100";
//        String file = "data/berlin52.tsp";

        Map<String,Integer> fileToFitness = new HashMap<>(5);


        String[] filesNamesEasy = {"berlin11_modified.tsp", "berlin52.tsp"};
        String[] filesNamesMedium = {"data/kroA200.tsp"}; //remove 100, 150 for 500


        for (String fileName : filesNamesMedium) {
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
    }
}
