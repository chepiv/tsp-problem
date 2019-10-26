import algorithm.AlgorithmRunner;
import algorithm.CrossoverType;
import algorithm.MutationType;
import algorithm.TravelingSalesman;
import org.apache.commons.io.FileUtils;
import utils.CsvWriter;

import java.io.File;
import java.util.*;

/**
 * Created by chepiv on 11/10/2019.
 * Contact: chepurin.ivan@gmail.com
 * Github:chepiv
 */
public class Main {
    public static void main(String[] args) throws Exception {
        TravelingSalesman algorithm;
        int[] generations = {500, 1000};
        int[] populationSizes = {100, 500, 1000};
        double[] pxRates = {0.5, 0.7, 1.0};
        double[] pmRates = {0.1, 0.15, 0.35};
        int startingCity = 0;
        int tournamentSize = 0;
        MutationType[] mutationTypes = {MutationType.SWAP, MutationType.INVERSE};
        CrossoverType[] crossoverTypes = {CrossoverType.PMX, CrossoverType.OX};
        CsvWriter csvWriter = new CsvWriter();
        String berlin52 = "berlin52";
        String gr666 = "gr666";
        String kroA100 = "kroA100";
//        String file = "data/berlin52.tsp";
        List<String> configurations = new ArrayList<>();
        configurations.add("dupa");

        Map<String,Integer> fileToFitness = new HashMap<>(5);


        String[] filesNamesEasy = {"berlin11_modified.tsp", "berlin52.tsp"};
        String[] filesNamesMedium = {"data/kroA100.tsp", "data/kroA150.tsp", "data/kroA200.tsp"};


        int i = 0;
        for (String fileName : filesNamesMedium) {
            for (int generation : generations) {
                for (int populationSize : populationSizes) {
                    for (double pm : pmRates) {
                        for (double px : pxRates) {
                            for (CrossoverType crossoverType : crossoverTypes) {
                                for (MutationType mutationType : mutationTypes) {
                                    tournamentSize =  (populationSize *10) /100;
                                    AlgorithmRunner algorithmRunner = new AlgorithmRunner(populationSize, generation, px, pm, tournamentSize, mutationType, crossoverType, fileName,i,fileToFitness);
                                    algorithmRunner.run();
                                    configurations.add(i + algorithmRunner.toString());
                                    i++;
                                }
                            }
                        }
                    }
                }
            }
            FileUtils.writeLines(new File("results/"+fileName+"_config.txt"),configurations);
        }
    }
}
