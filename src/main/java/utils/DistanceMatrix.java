package utils;

import model.City;

import java.util.List;

/**
 * Created by chepiv on 11/10/2019.
 * Contact: chepurin.ivan@gmail.com
 * Github:chepiv
 */
public class DistanceMatrix {

    private List<City> cities;
    private int dimension;
    private double[][] matrix;


    public DistanceMatrix(List<City> cities) {
        this.cities = cities;
        dimension = cities.size();
        matrix = new double[dimension][dimension];
    }

    public double[][] calculateMatrix() {
        for (int i = 0; i < cities.size(); i++) {
            matrix[i][i] = 0.0;
            for (int j = i+1; j < cities.size(); j++) {
                double distance = DistanceCalculator.calculateDistance(cities.get(i), cities.get(j));
                matrix[i][j] = distance;
                matrix[j][i] = distance;
            }
        }
        initMatrix(matrix);
        return matrix;
    }


    private void initMatrix(double[][] matrix){
        MatrixSingleton.getInstance().setDistances(matrix);
    }

}
