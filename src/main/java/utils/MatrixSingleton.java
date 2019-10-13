package utils;

/**
 * Created by chepiv on 11/10/2019.
 * Contact: chepurin.ivan@gmail.com
 * Github:chepiv
 */
public class MatrixSingleton {

    private final static MatrixSingleton instance = new MatrixSingleton();
    private double[][] distances;

    private MatrixSingleton(){}

    public static MatrixSingleton getInstance() {
        return instance;
    }

    public void setDistances(double[][] distances) {
        this.distances = distances;
    }

    public double[][] getDistances() {
        return distances;
    }
}
