package algorithm;

import com.google.common.collect.Lists;
import model.Genome;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by chepiv on 16/10/2019.
 * Contact: chepurin.ivan@gmail.com
 * Github:chepiv
 */
public class TravelingSalesman {

    private int generationSize;
    private int genomeSize;
    private int numberOfCities;
    /**
     * Reproduction size liczba rodzicow
     */
    private int reproductionSize;
    private int maxIterations;
    private float mutationRate;
    private int[][] travelPrices;
    private int startingCity;
    private int targetFitness;
    private int tournamentSize;

    public TravelingSalesman(int genomeSize, int reproductionSize, int tournamentSize) {
        this.genomeSize = genomeSize;
        this.reproductionSize = reproductionSize;
        this.tournamentSize = tournamentSize;
    }


    public List<Genome> selection(List<Genome> population) {
        List<Genome> selected = new ArrayList<>();
        for (int i = 0; i < reproductionSize; i++) {
            selected.add(tournamentSelection(population));
        }
        return selected;
    }

    public static  List<Genome> pickNRandomElements(List<Genome> list, int n) {
        Collections.shuffle(list);
        return list.stream()
                .limit(n)
                .collect(Collectors.toList());
    }

    public Genome tournamentSelection(List<Genome> population) {
        List<Genome> selected = pickNRandomElements(population, tournamentSize);
        return Collections.min(selected);
    }

    public List<Genome> crossover(List<Genome> parents){
        int crossingPoint = new Random().nextInt(genomeSize);

        Genome dad = parents.get(0);
        Genome mom = parents.get(1);

        List<Integer> firstDadPart = dad.getRoute().subList(0, crossingPoint);
        List<Integer> secondDadPart = dad.getRoute().subList(crossingPoint, genomeSize);


        List<Integer> firstMomPart = mom.getRoute().subList(0, crossingPoint);
        List<Integer> secondMomPart = mom.getRoute().subList(crossingPoint, genomeSize);

        List<Integer> childRoute1 = Stream
                .concat(firstDadPart.stream(),secondMomPart.stream())
                .collect(Collectors.toList());

        Genome child1 = new Genome(childRoute1, 0, childRoute1.size());

        List<Integer> childRoute2 = Stream
                .concat(firstMomPart.stream(),secondDadPart.stream())
                .collect(Collectors.toList());

        Genome child2 = new Genome(childRoute2, 0, childRoute1.size());

        return Lists.newArrayList(child1,child2);

    }

}
