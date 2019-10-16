import model.City;
import model.Genome;
import org.junit.Test;
import utils.DataParser;
import utils.DistanceMatrix;

import java.io.IOException;
import java.util.List;

/**
 * Created by chepiv on 11/10/2019.
 * Contact: chepurin.ivan@gmail.com
 * Github:chepiv
 */
public class DataParserTest {


    @Test
    public void loadData_shouldParseTspToCities() throws IOException {
        List<City> cities = DataParser.loadData("C:\\Users\\chepiv\\IdeaProjects\\tsp-problem\\data\\berlin11_modified.tsp");

        cities.stream().forEach(System.out::println);

        DistanceMatrix distanceMatrix = new DistanceMatrix(cities);

        distanceMatrix.calculateMatrix();


        Genome genome = new Genome(0,cities.size());

        System.out.println(genome);;
    }
}