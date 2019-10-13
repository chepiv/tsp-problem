import model.City;
import utils.DataParser;
import utils.DistanceMatrix;
import utils.MatrixSingleton;

import java.io.IOException;
import java.util.List;

/**
 * Created by chepiv on 11/10/2019.
 * Contact: chepurin.ivan@gmail.com
 * Github:chepiv
 */
public class Main {
    public static void main(String[] args) throws IOException {

        List<City> cities = DataParser.loadData("C:\\Users\\chepiv\\IdeaProjects\\tsp-problem\\data\\berlin11_modified.tsp");

        DistanceMatrix distanceMatrix = new DistanceMatrix(cities);
        distanceMatrix.calculateMatrix();

        MatrixSingleton matrixSingleton = MatrixSingleton.getInstance();


    }
}
