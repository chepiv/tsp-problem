package com.chepiv.algorithm;

import com.google.common.collect.Lists;
import com.chepiv.model.Genome;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
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
        int crossingPoint3 = new Random().nextInt(genomeSize - 1);
        int crossingPoint4 = new Random().nextInt(genomeSize - 1);

        int start = Math.min(crossingPoint1, crossingPoint2);
        int end = Math.max(crossingPoint1, crossingPoint2);
        int start1 = Math.min(crossingPoint3, crossingPoint4);
        int end1 = Math.max(crossingPoint3, crossingPoint4);

        Genome dad = parents.get(0);
        Genome mom = parents.get(1);

        List<Integer> dadRoute = dad.getRoute();
        List<Integer> momRoute = mom.getRoute();

        List<Integer> mainPart = dadRoute.subList(start, end);
        List<Integer> mainPart2 = momRoute.subList(start1, end1);

        Integer [] childRouteArray = new Integer[genomeSize];
        Arrays.fill(childRouteArray,-1);
        List<Integer> childRoute = Arrays.asList(childRouteArray);

        Integer [] childRouteArray2 = new Integer[genomeSize];
        Arrays.fill(childRouteArray2,-1);
        List<Integer> childRoute2 = Arrays.asList(childRouteArray2);

        int j = 0;
        for (int i = start; i <end ; i++) {
            childRoute.set(i,mainPart.get(j));
            j++;
        }

        int q = 0;
        for (int i = start1; i <end1 ; i++) {
            childRoute2.set(i,mainPart2.get(q));
            q++;
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

    public List<Genome> pmxCrossover(List<Genome> parents){
        int crossingPoint1 = new Random().nextInt(genomeSize - 1);
        int crossingPoint2 = new Random().nextInt(genomeSize - 1);
        int crossingPoint3 = new Random().nextInt(genomeSize - 1);
        int crossingPoint4 = new Random().nextInt(genomeSize - 1);

        int start = Math.min(crossingPoint1, crossingPoint2);
        int end = Math.max(crossingPoint1, crossingPoint2);
        int start1 = Math.min(crossingPoint3, crossingPoint4);
        int end1 = Math.max(crossingPoint3, crossingPoint4);

        Genome dad = parents.get(0);
        Genome mom = parents.get(1);

        List<Integer> dadRoute = dad.getRoute();
        List<Integer> momRoute = mom.getRoute();

        List<Integer> mainPart = dadRoute.subList(start, end);
        List<Integer> mainPart2 = momRoute.subList(start, end);

        Map<Integer,Integer> dadToMomMap = new HashMap<>();
        for (int i = 0; i < mainPart.size(); i++) {
            dadToMomMap.put(mainPart.get(i),mainPart2.get(i));
        }

        Map<Integer,Integer> momToDadMap = new HashMap<>();
        for (int i = 0; i < mainPart2.size(); i++) {
            momToDadMap.put(mainPart2.get(i),mainPart.get(i));
        }

        Integer [] childRouteArray = new Integer[genomeSize];
        Arrays.fill(childRouteArray,-1);
        List<Integer> childRoute = Arrays.asList(childRouteArray);

        Integer [] childRouteArray2 = new Integer[genomeSize];
        Arrays.fill(childRouteArray2,-1);
        List<Integer> childRoute2 = Arrays.asList(childRouteArray2);

        int j = 0;
        for (int i = start; i <end ; i++) {
            childRoute.set(i,mainPart.get(j));
            j++;
        }

        int k = 0;
        for (int i = start; i <end ; i++) {
            childRoute2.set(i,mainPart2.get(k));
            k++;
        }

        for (int i = 0; i < childRoute.size(); i++) {
            if (childRoute.get(i) == -1) {
                if (!childRoute.contains(momRoute.get(i))) {
                    childRoute.set(i,momRoute.get(i));
                }
                else {
                    if (!childRoute.contains(dadToMomMap.get(momRoute.get(i)))){
                        childRoute.set(i,dadToMomMap.get(momRoute.get(i)));
                    }else {
                        for (Integer key : dadToMomMap.keySet()) {
                            if (!childRoute.contains(dadToMomMap.get(key))){
                                childRoute.set(i,dadToMomMap.get(key));
                            }
                        }
                    }
                }
            }
        }


        for (int i = 0; i < childRoute2.size(); i++) {
            if (childRoute2.get(i) == -1) {
                if (!childRoute2.contains(dadRoute.get(i))) {
                    childRoute2.set(i,dadRoute.get(i));
                }
                else {
                    if (!childRoute2.contains(momToDadMap.get(dadRoute.get(i)))){
                        childRoute2.set(i,momToDadMap.get(dadRoute.get(i)));
                    }else {
                        for (Integer key : momToDadMap.keySet()) {
                            if (!childRoute2.contains(momToDadMap.get(key))){
                                childRoute2.set(i,momToDadMap.get(key));
                            }
                        }
                    }
                }
            }
        }

        System.out.println(dadRoute);
        System.out.println(momRoute);
        System.out.println(childRoute);
        System.out.println(childRoute2);


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

    private void inverseMutation(Genome genome) {
        Random random = new Random();
        List<Integer> route = genome.getRoute();
        int firstPoint = random.nextInt(route.size() - 1);
        int secondPoint = random.nextInt(route.size() - 1);
        int start = Math.min(firstPoint, secondPoint);
        int end = Math.max(firstPoint, secondPoint);

        Collections.reverse(genome.getRoute().subList(start,end));
    }

    public void swap(List<Genome> genomes, double pm){
        double rand = ThreadLocalRandom.current().nextDouble(0, 1);

        for (Genome genome : genomes) {
            if (rand <= pm) {
                swapMutation(genome);
                genome.recalculateFitness();
            }
        }

    }

    public void inverse(List<Genome> genomes, double pm) {
        double rand = ThreadLocalRandom.current().nextDouble(0, 1);
        for (Genome genome : genomes) {
            if (rand <= pm) {
                inverseMutation(genome);
                genome.recalculateFitness();
            }
        }
    }

}
