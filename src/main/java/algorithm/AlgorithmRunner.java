package algorithm;

import com.github.sh0nk.matplotlib4j.Plot;
import com.github.sh0nk.matplotlib4j.PythonExecutionException;
import com.google.common.collect.Lists;
import model.City;
import model.Genome;
import utils.DataParser;
import utils.DistanceMatrix;
import utils.MatrixSingleton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by chepiv on 21/10/2019.
 * Contact: chepurin.ivan@gmail.com
 * Github:chepiv
 */
public class AlgorithmRunner {

    private TravelingSalesman algorithm;
    private int populationSize = 100;
    private int generations = 10;
    private int startingCity = 0;
    private double px = 0.7;
    private double pm = 0.02;


    public void run() throws IOException, PythonExecutionException {
        List<City> cities = DataParser.loadData("C:\\Users\\chepiv\\IdeaProjects\\tsp-problem\\data\\berlin11_modified.tsp");

        DistanceMatrix distanceMatrix = new DistanceMatrix(cities);
        distanceMatrix.calculateMatrix();

        MatrixSingleton matrixSingleton = MatrixSingleton.getInstance();

        //GENERATING RANDOM FIRST POPULATION

        List <Genome> population = Lists.newArrayList();
        int numberOfCities = cities.size();
        for (int i = 0; i < populationSize; i++) {
            population.add(new Genome(startingCity, numberOfCities));
        }

        int reproductionSize = 2;
        int tournamentSize = 10;
        int genomeSize = population.get(0).getRoute().size();
        algorithm = new TravelingSalesman(genomeSize, numberOfCities, reproductionSize, startingCity, tournamentSize);

        List<Integer> generationsX = Lists.newArrayList();
        List<Integer> fitnesesY = Lists.newArrayList();

        for (int i = 0; i < generations; i++) {
            List<Genome> selectedAndCrossedPopulation = fillPopulation(population);
            mutatePopulation(selectedAndCrossedPopulation);
            Genome bestGenome = Collections.min(selectedAndCrossedPopulation);
            System.out.println("BEST GENOME");
            System.out.println(bestGenome);

            generationsX.add(i);
            fitnesesY.add(bestGenome.getFitness());

            population = selectedAndCrossedPopulation;
        }
        Plot plt = Plot.create();
        plt.plot()
                .add(generationsX,fitnesesY)
                .linestyle("--");

        plt.show();

    }

    private List<Genome> fillPopulation(List<Genome> population){
        System.out.println("SELECTION PROCESS HAS STARTED");
        List<Genome> newPopulation = new ArrayList<>();
        while (!isPopulationFilled(newPopulation)) {
            List<Genome> parents = algorithm.selection(population);

            double rand = ThreadLocalRandom.current().nextDouble(0, 1);
            if (rand <= px){
                System.out.println("CROSSOVER PROCESS HAS STARTED");
                List<Genome> kids = algorithm.orderedCrossover(parents);
                kids.forEach(System.out::println);
                newPopulation.addAll(kids);
            }else {
                newPopulation.addAll(parents);
            }
        }
        return newPopulation;
    }

    private void mutatePopulation (List<Genome> population) {
            System.out.println("MUTATION PROCESS HAS STARTED");
            algorithm.swap(population, pm);
    }

    private boolean isPopulationFilled(List<Genome> population) {
        return population.size() == populationSize;
    }
}
