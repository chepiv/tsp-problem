package com.chepiv.utils;

import com.chepiv.model.City;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chepiv on 08/10/2019.
 * Contact: chepurin.ivan@gmail.com
 * Github:chepiv
 */
public class DataParser {

    public static List<City> loadData(String path) throws IOException {

        BufferedReader reader = new BufferedReader(new FileReader(new File(path)));

        String name = reader.readLine();
        String type = reader.readLine();
        String comment = reader.readLine();
        int dimensions = Integer.parseInt(reader.readLine().split("\\s+")[1]);
        String edge_weight_type = reader.readLine() ;
        String displayDataType = reader.readLine();
        reader.readLine(); // nodes header

        List<City> cities = new ArrayList<>();

        for (int i = 0; i < dimensions; i++) {
            String line = reader.readLine();
            if (line.equals("EOF")) break;

            String[] split = line.split("\\s+");
            cities.add(new City(Double.parseDouble(split[1]), Double.parseDouble(split[2])));


        }

        return cities;
    }

}
