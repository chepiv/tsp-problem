import model.City;
import org.junit.Test;
import utils.DataParser;

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
        List<City> cities = DataParser.loadData("C:\\Users\\chepiv\\IdeaProjects\\tsp-problem\\data\\ali535.tsp");

        cities.stream().forEach(System.out::println);
    }
}