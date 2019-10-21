import algorithm.TravelingSalesman;
import com.google.common.collect.Lists;
import model.City;
import model.Genome;
import utils.DataParser;
import utils.DistanceMatrix;
import utils.MatrixSingleton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chepiv on 11/10/2019.
 * Contact: chepurin.ivan@gmail.com
 * Github:chepiv
 */
public class Main {
    public static void main(String[] args) throws IOException {

        List<City> cities = DataParser.loadData("C:\\Users\\chepiv\\IdeaProjects\\tsp-problem\\data\\berlin11_modified.tsp");

        DistanceMatrix distanceMatrix = new DistanceMatrix(cities);
        distanceMatrix.calculateMatrix();

        MatrixSingleton matrixSingleton = MatrixSingleton.getInstance();
        int startingCity = 0;


        int populationSize = 20;
        ArrayList<Genome> population = Lists.newArrayList();
        int numberOfCities = cities.size();
        for (int i = 0; i < populationSize; i++) {
            population.add(new Genome(startingCity, numberOfCities));
        }

        int reproductionSize = 2;
        int tournamentSize = 3;
        int genomeSize = population.get(0).getRoute().size();
        TravelingSalesman algorithm = new TravelingSalesman(genomeSize, numberOfCities, reproductionSize, startingCity, tournamentSize);

        List<Genome> selection = algorithm.selection(population);

        selection.forEach(System.out::println);

        System.out.println();

        algorithm.orderedCrossover(selection).forEach(System.out::println);

    }
}
