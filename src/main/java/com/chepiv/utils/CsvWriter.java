package com.chepiv.utils;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;

import java.io.FileWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Created by chepiv on 25/10/2019.
 * Contact: chepurin.ivan@gmail.com
 * Github:chepiv
 */
public class CsvWriter {
    public void writeCsvFromBean(Path path, List<CsvResultLine> results) throws Exception {
        Files.createDirectories(path.getParent());
        Writer writer  = new FileWriter(path.toString());

        final CustomMappingStrategy<CsvResultLine> mappingStrategy = new CustomMappingStrategy<>();
        mappingStrategy.setType(CsvResultLine.class);
        StatefulBeanToCsv sbc = new StatefulBeanToCsvBuilder(writer)
                .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                .build();


        sbc.write(results);
        writer.close();
    }
}
