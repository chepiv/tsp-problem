package com.chepiv.model;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Created by chepiv on 13/10/2019.
 * Contact: chepurin.ivan@gmail.com
 * Github:chepiv
 */
public class GenomeTest {

    @Test
    public void calculateFitness_shouldDoItRight() {
        ArrayList<Integer> route = Lists.newArrayList(1, 2);
        double[][] distances = {{0,1,3},
                                {1,0,2},
                                {3,2,0}};
        Genome genome = new Genome(route, 0, route.size() + 1, distances);
        genome.recalculateFitness();

        assertEquals(6,genome.getFitness());
    }
}