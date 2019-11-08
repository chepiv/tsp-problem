package com.chepiv.algorithm;

import com.chepiv.model.City;
import com.chepiv.model.Genome;
import com.chepiv.utils.DataParser;
import com.chepiv.utils.DistanceMatrix;
import com.github.sh0nk.matplotlib4j.Plot;
import com.github.sh0nk.matplotlib4j.PythonExecutionException;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.style.markers.SeriesMarkers;

import java.io.IOException;
import java.util.*;

/**
 * Created by chepiv on 30/10/2019.
 * Contact: chepurin.ivan@gmail.com
 * Github:chepiv
 */
public interface Algorithm {

    int startingCity = 0;

    void run() throws Exception;

    default Genome getRandomIndividual(int startingCity, List<City> cities) {
        return new Genome(startingCity, cities.size());
    }

    default List<City> getCities(String filename) throws IOException {
        List<City> cities = DataParser.loadData(filename);
        int numberOfCities = cities.size();
        DistanceMatrix distanceMatrix = new DistanceMatrix(cities);
        distanceMatrix.calculateMatrix();
        return cities;
    }

    default Genome getNeighbour(Genome genome, int numberOfCities) {
        Random random = new Random();
        List<Integer> route = new ArrayList<>(genome.getRoute());
        Collections.swap(route, random.nextInt(route.size() - 1), random.nextInt(route.size() - 1));
        return new Genome(route, startingCity, numberOfCities);
    }

    default void drawChart(List<Integer> candidates, List<Integer> bestInds, List<Double> average, List<Integer> worst) {

        // Create Chart
        XYChart chart = new XYChartBuilder().width(1600).height(800).title("Area Chart").xAxisTitle("Generation").yAxisTitle("Fitness").build();
        Optional.ofNullable(candidates).map(c -> chart.addSeries("candidates",c).setMarker(SeriesMarkers.NONE).setLabel("candidates"));
        Optional.ofNullable(bestInds).map(b -> chart.addSeries("bestInds",b).setMarker(SeriesMarkers.NONE).setLabel("best"));
        Optional.ofNullable(average).map(a -> chart.addSeries("average",a).setMarker(SeriesMarkers.NONE).setLabel("average"));
        Optional.ofNullable(worst).map(w -> chart.addSeries("worst",w).setMarker(SeriesMarkers.NONE).setLabel("worst"));
        chart.getStyler().setLegendVisible(true);


        new SwingWrapper(chart).displayChart();

    }

    default void drawPlot(List<Integer> bestFitnessesHistory, List<Integer> generationsHistory) throws IOException, PythonExecutionException {
        Plot plt = Plot.create();
        plt.plot()
                .add(generationsHistory, bestFitnessesHistory)
                .linestyle("--");

        plt.show();
    }
}
