package com.chepiv;

import com.chepiv.model.City;
import com.chepiv.model.Genome;
import com.chepiv.utils.DataParser;
import com.chepiv.utils.DistanceMatrix;
import com.chepiv.utils.MatrixSingleton;
import com.google.common.collect.Lists;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.OptionalDouble;

/**
 * Created by chepiv on 20/11/2019.
 * Contact: chepurin.ivan@gmail.com
 * Github:chepiv
 */
public class RandomMain {
    public static void main(String[] args) throws IOException {
        run();
    }

    private static void run() throws IOException {
        String file = "data/kroA200.tsp";
        List<City> cities = DataParser.loadData(file);

        DistanceMatrix distanceMatrix = new DistanceMatrix(cities);
        distanceMatrix.calculateMatrix();

        MatrixSingleton matrixSingleton = MatrixSingleton.getInstance();
        List<Double> best = new ArrayList<>();
        List<Double> worst = new ArrayList<>();


        //GENERATING RANDOM FIRST POPULATION


        for (int z = 0;  z< 10; z++) {
            List<Genome> population = Lists.newArrayList();
            int numberOfCities = cities.size();
            for (int i = 0; i < 10_000; i++) {
                population.add(new Genome(0, numberOfCities));
            }

            StandardDeviation sd = new StandardDeviation();
            double evaluate = sd.evaluate(population.stream().map(Genome::getFitness).mapToDouble(Integer::doubleValue).toArray());
            Integer min = Collections.min(population).getFitness();
            best.add(min.doubleValue());
            Integer worstG = Collections.max(population).getFitness();
            worst.add(worstG.doubleValue());
            System.out.println("BEST FITNESS: " + min);
            System.out.println("AVERAGE:" + population.stream().map(Genome::getFitness).mapToDouble(Integer::doubleValue).average());
            System.out.println("DIVIATION: " + evaluate);
        }

        OptionalDouble bestAverage = best.stream().mapToDouble(Double::doubleValue).average();
        OptionalDouble worstAverage = worst.stream().mapToDouble(Double::doubleValue).average();

        StandardDeviation sd = new StandardDeviation();
        double evaluate = sd.evaluate(best.stream().mapToDouble(Double::doubleValue).toArray());

        System.out.println("BEST FITNESS: " + Collections.min(best));
        best.stream().forEach(System.out::println);
        System.out.println("AVERAGE:" + best.stream().mapToDouble(Double::doubleValue).average());
        System.out.println("DIVIATION: " + evaluate);
        System.out.println("Worst:" + worstAverage);

    }
}
