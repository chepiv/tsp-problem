package algorithm;

import com.google.common.collect.Lists;
import model.City;
import model.Genome;
import utils.*;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.OptionalDouble;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by chepiv on 21/10/2019.
 * Contact: chepurin.ivan@gmail.com
 * Github:chepiv
 */
public class AlgorithmRunner {

    private TravelingSalesman algorithm;
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
    String file = berlin52;

    public void run() throws Exception {

        List<City> cities = DataParser.loadData("C:\\Users\\chepiv\\IdeaProjects\\tsp-problem\\data\\" + file+".tsp");

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

        int genomeSize = population.get(0).getRoute().size();
        algorithm = new TravelingSalesman(genomeSize, numberOfCities, reproductionSize, startingCity, tournamentSize);

        List<Integer> generationsX = Lists.newArrayList();
        List<Integer> fitnesesY = Lists.newArrayList();
        List<CsvResultLine> csvResults = Lists.newArrayList();

        for (int i = 0; i < generations; i++) {
            List<Genome> selectedAndCrossedPopulation = fillPopulation(population);
            mutatePopulation(selectedAndCrossedPopulation,mutationType);
            Genome bestGenome = Collections.min(selectedAndCrossedPopulation);
            Genome worthGenome = Collections.max(selectedAndCrossedPopulation);
            OptionalDouble averageGenome = selectedAndCrossedPopulation
                    .stream()
                    .mapToDouble(Genome::getFitness)
                    .average();
            Double average = averageGenome.isPresent() ? averageGenome.getAsDouble(): null;
            System.out.println("BEST GENOME");
            System.out.println(bestGenome);

            generationsX.add(i);
            fitnesesY.add(bestGenome.getFitness());

            csvResults.add(new CsvResultLine(i, bestGenome.getFitness(),average,worthGenome.getFitness()));

            population = selectedAndCrossedPopulation;
        }

        csvWriter.writeCsvFromBean(Paths.get("C:\\Users\\chepiv\\IdeaProjects\\tsp-problem\\results\\"+file+".csv"),csvResults);
//        Plot plt = Plot.create();
//        plt.plot()
//                .add(generationsX,fitnesesY)
//                .linestyle("--");
//
//        plt.show();


    }

    private List<Genome> fillPopulation(List<Genome> population){
        System.out.println("SELECTION PROCESS HAS STARTED");
        List<Genome> newPopulation = new ArrayList<>();
        while (!isPopulationFilled(newPopulation)) {
            List<Genome> parents = algorithm.selection(population);

            double rand = ThreadLocalRandom.current().nextDouble(0, 1);
            if (rand <= px){
                System.out.println("CROSSOVER PROCESS HAS STARTED");
                List<Genome> kids = crossover(parents,crossoverType);
                kids.forEach(System.out::println);
                newPopulation.addAll(kids);
            }else {
                newPopulation.addAll(parents);
            }
        }
        return newPopulation;
    }

    private List<Genome> crossover(List<Genome> population, CrossoverType crossoverType) {
        if (crossoverType == CrossoverType.OX) {
            return algorithm.orderedCrossover(population);
        }
        else return algorithm.pmxCrossover(population);
    }

    private void mutatePopulation (List<Genome> population, MutationType mutationType) {
            System.out.println("MUTATION PROCESS HAS STARTED");
            if (mutationType == MutationType.SWAP) {
                algorithm.swap(population, pm);
            }
            else algorithm.inverse(population,pm);
    }

    private boolean isPopulationFilled(List<Genome> population) {
        return population.size() == populationSize;
    }
}
