package algorithm;

import model.Genome;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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
     * Reproduction size is the number of genomes who'll be selected to reproduce to make the next generation
     */
    private int reproductionSize;
    private int maxIterations;
    private float mutationRate;
    private int[][] travelPrices;
    private int startingCity;
    private int targetFitness;
    private int tournamentSize;
    private SelectionType selectionType;



    // We select reproductionSize genomes based on the method
    // predefined in the attribute selectionType
    public List<Genome> selection(List<Genome> population) {
        List<Genome> selected = new ArrayList<>();
        Genome winner;
        for (int i=0; i < reproductionSize; i++) {
            if (selectionType == SelectionType.ROULETTE) {
                selected.add(rouletteSelection(population));
            }
            else if (selectionType == SelectionType.TOURNAMENT) {
                selected.add(tournamentSelection(population));
            }
        }

        return selected;
    }

    public Genome rouletteSelection(List<Genome> population) {
        int totalFitness = population.stream().map(Genome::getFitness).mapToInt(Integer::intValue).sum();

        // We pick a random value - a point on our roulette wheel
        Random random = new Random();
        int selectedValue = random.nextInt(totalFitness);

        // Because we're doing minimization, we need to use reciprocal
        // value so the probability of selecting a genome would be
        // inversely proportional to its fitness - the smaller the fitness
        // the higher the probability
        float recValue = (float) 1/selectedValue;

        // We add up values until we reach out recValue, and we pick the
        // genome that crossed the threshold
        float currentSum = 0;
        for (Genome genome : population) {
            currentSum += (float) 1/genome.getFitness();
            if (currentSum >= recValue) {
                return genome;
            }
        }

        // In case the return didn't happen in the loop above, we just
        // select at random
        int selectRandom = random.nextInt(generationSize);
        return population.get(selectRandom);
    }

    // A helper function to pick n random elements from the population
// so we could enter them into a tournament
    public static <E> List<E> pickNRandomElements(List<E> list, int n) {
        Random r = new Random();
        int length = list.size();

        if (length < n) return null;

        for (int i = length - 1; i >= length - n; --i) {
            Collections.swap(list, i , r.nextInt(i + 1));
        }
        return list.subList(length - n, length);
    }

    // A simple implementation of the deterministic tournament - the best genome
    // always wins
    public Genome tournamentSelection(List<Genome> population) {
        List<Genome> selected = pickNRandomElements(population, tournamentSize);
        return Collections.min(selected);
    }

}
