import algorithm.TravelingSalesman;
import com.google.common.collect.Lists;
import model.City;
import model.Genome;
import org.apache.log4j.Logger;
import utils.DataParser;
import utils.DistanceMatrix;
import utils.MatrixSingleton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by chepiv on 11/10/2019.
 * Contact: chepurin.ivan@gmail.com
 * Github:chepiv
 */
public class Main {
    private static Logger LOG = Logger.getLogger(Main.class);
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

        System.out.println("SELECTION PROCESS HAS STARTED");
        List<Genome> selection = algorithm.selection(population);
        selection.forEach(System.out::println);
        System.out.println();


        System.out.println("CROSSOVER PROCESS HAS STARTED");
        //TODO: CROSSOVER IS FUCKED UP. MAYBE 2 CHILDREN IS PROBLEM
        List<Genome> crossover = algorithm.orderedCrossover(selection);
        crossover.forEach(System.out::println);

        System.out.println("MUTATION PROCESS HAS STARTED");
        List<Genome> mutated = algorithm.swap(crossover);
        mutated.forEach(System.out::println);

        List <Genome>generation = new ArrayList();
        generation.addAll(crossover);
        generation.addAll(mutated);


        Genome bestGenome = Collections.min(generation);

        System.out.println("BEST GENOME");
        System.out.println(bestGenome);

    }
}
