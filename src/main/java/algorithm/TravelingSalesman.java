package algorithm;

import com.google.common.collect.Lists;
import model.Genome;

import java.util.*;
import java.util.stream.Collectors;

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

    public TravelingSalesman(int genomeSize, int reproductionSize, int startingCity, int tournamentSize) {
        this.genomeSize = genomeSize;
        this.reproductionSize = reproductionSize;
        this.startingCity = startingCity;
        this.tournamentSize = tournamentSize;
    }


    public List<Genome> selection(List<Genome> population) {
        List<Genome> selected = new ArrayList<>();
        for (int i = 0; i < reproductionSize; i++) {
            selected.add(tournamentSelection(population));
        }
        return selected;
    }

    public static List<Genome> pickNRandomElements(List<Genome> list, int n) {
        Collections.shuffle(list);
        return list.stream()
                .limit(n)
                .collect(Collectors.toList());
    }

    public Genome tournamentSelection(List<Genome> population) {
        List<Genome> selected = pickNRandomElements(population, tournamentSize);
        return Collections.min(selected);
    }

    public List<Genome> orderedCrossover(List<Genome> parents) {
        int crossingPoint1 = new Random().nextInt(genomeSize - 1);
        int crossingPoint2 = new Random().nextInt(genomeSize - 1);

        int start = Math.min(crossingPoint1, crossingPoint2);
        int end = Math.max(crossingPoint1, crossingPoint2);

        Genome dad = parents.get(0);
        Genome mom = parents.get(1);

        List<Integer> dadRoute = dad.getRoute();
        List<Integer> momRoute = mom.getRoute();

        List<Integer> mainPart = dadRoute.subList(start, end);

        Integer [] childRouteArray = new Integer[genomeSize];
        Arrays.fill(childRouteArray,-1);
        List<Integer> childRoute = Arrays.asList(childRouteArray);

        int j = 0;
        for (int i = start; i <end ; i++) {
            childRoute.set(i,mainPart.get(j));
            j++;
        }

        //TODO: change to check if present from HASHSET
        for (int i = momRoute.size() - 1; i > 0; i--) {
             if (childRoute.get(i) == -1){
                 if (!childRoute.contains(momRoute.get(i))) {
                     childRoute.set(i, momRoute.get(i));
                 }
             }
        }
        Genome child = new Genome(childRoute, startingCity, numberOfCities);
        return Lists.newArrayList(child);
    }

}
