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

    public TravelingSalesman(int genomeSize, int numberOfCities, int reproductionSize, int startingCity, int tournamentSize) {
        this.genomeSize = genomeSize;
        this.numberOfCities = numberOfCities;
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
        List<Integer> mainPart2 = momRoute.subList(start, end);

        Integer [] childRouteArray = new Integer[genomeSize];
        Arrays.fill(childRouteArray,-1);
        List<Integer> childRoute = Arrays.asList(childRouteArray);

        Integer [] childRouteArray2 = new Integer[genomeSize];
        Arrays.fill(childRouteArray2,-1);
        List<Integer> childRoute2 = Arrays.asList(childRouteArray2);

        int j = 0;
        for (int i = start; i <end ; i++) {
            childRoute.set(i,mainPart.get(j));
            childRoute2.set(i,mainPart2.get(j));
            j++;
        }


        //TODO: change to check if present from HASHSET
        for (int i = momRoute.size() - 1; i >= 0; i--) {
             if (childRoute.get(i) == -1){
                 for (int k = momRoute.size() - 1; k >= 0; k--) {
                     if (!childRoute.contains(momRoute.get(k))){
                         childRoute.set(i, momRoute.get(k));
                         break;
                     }
                 }
             }
        }

        for (int i = dadRoute.size() - 1; i >= 0; i--) {
             if (childRoute2.get(i) == -1){
                 for (int k = dadRoute.size() - 1; k >= 0; k--) {
                     if (!childRoute2.contains(dadRoute.get(k))){
                         childRoute2.set(i, dadRoute.get(k));
                         break;
                     }
                 }
             }
        }

//        printValuesOfCrossover(dadRoute, momRoute, childRoute, childRoute2);

        Genome child = new Genome(childRoute, startingCity, numberOfCities);
        Genome child2 = new Genome(childRoute2, startingCity, numberOfCities);
        return Lists.newArrayList(child,child2);
    }

    private void printValuesOfCrossover(List<Integer> dadRoute, List<Integer> momRoute, List<Integer> childRoute, List<Integer> childRoute2) {
        System.out.println("DAD");
        System.out.println(dadRoute);
        System.out.println("MOM");
        System.out.println(momRoute);
        System.out.println("CHILD");
        System.out.println(childRoute);
        System.out.println("CHILD");
        System.out.println(childRoute2);
    }

    //TODO: mutation doesn't count fitness because it's counted in constructor and it's void here
    private void swapMutation(Genome genome){
        Random random = new Random();
        List<Integer> route = genome.getRoute();
        Collections.swap(route, random.nextInt(route.size()-1),random.nextInt(route.size()-1));
    }

    public List<Genome> swap(List<Genome> genomes){
        genomes.forEach(this::swapMutation);
        List<Genome> mutatedGenomes = new ArrayList<>();

        for (Genome genome : genomes) {
            Genome g = new Genome(genome.getRoute(), startingCity, numberOfCities);
            mutatedGenomes.add(g);
        }
        return mutatedGenomes;
    }

}
