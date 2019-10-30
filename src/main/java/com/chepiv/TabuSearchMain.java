package com.chepiv;

import com.chepiv.algorithm.TabuSearchAlgorithm;
import com.github.sh0nk.matplotlib4j.PythonExecutionException;

import java.io.IOException;

/**
 * Created by chepiv on 30/10/2019.
 * Contact: chepurin.ivan@gmail.com
 * Github:chepiv
 */
public class TabuSearchMain {
    public static void main(String[] args) throws IOException, PythonExecutionException {
        int tabuListSize = 200;
        int numOfNeighbours = 3000;
        int maxIterations = 2000;
        String fileName = "data/kroA100.tsp";
        int startingCity = 0;
        TabuSearchAlgorithm tabuSearchAlgorithm = new TabuSearchAlgorithm(tabuListSize, numOfNeighbours, maxIterations, fileName, startingCity);
        tabuSearchAlgorithm.run();
    }
}
