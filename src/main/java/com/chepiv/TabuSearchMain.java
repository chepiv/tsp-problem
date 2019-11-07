package com.chepiv;

import com.chepiv.algorithm.TabuSearchAlgorithm;

/**
 * Created by chepiv on 30/10/2019.
 * Contact: chepurin.ivan@gmail.com
 * Github:chepiv
 */
public class TabuSearchMain {
    public static void main(String[] args) throws Exception {
        int tabuListSize = 10;
        int numOfNeighbours = 20;
        int maxIterations = 2000;
        String fileName = "data/berlin52.tsp";
        int startingCity = 0;
        TabuSearchAlgorithm tabuSearchAlgorithm = new TabuSearchAlgorithm(tabuListSize, numOfNeighbours, maxIterations, fileName, startingCity);
        tabuSearchAlgorithm.run();
    }
}
