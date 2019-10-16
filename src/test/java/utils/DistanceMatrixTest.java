package utils;

import com.google.common.collect.Lists;
import model.City;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

/**
 * Created by chepiv on 11/10/2019.
 * Contact: chepurin.ivan@gmail.com
 * Github:chepiv
 */
public class DistanceMatrixTest {

    DistanceMatrix distanceMatrix;

    @Before
    public void setUp() throws Exception {
        City one = new City(1,2);
        City two = new City(3,4);
        City three = new City(5,6);
        distanceMatrix = new DistanceMatrix(Lists.newArrayList(one,two,three));
    }

    @Test
    public void calculateMatrix() {
        double[][] matrix = distanceMatrix.calculateMatrix();

        System.out.println(Arrays.deepToString(matrix).replace("], ", "]\n"));





    }
}