package utils;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;

/**
 * Created by chepiv on 25/10/2019.
 * Contact: chepurin.ivan@gmail.com
 * Github:chepiv
 */
public class CsvResultLine{

    public CsvResultLine(int generation, Integer worstResult, Double averageResult, Integer bestResult) {
        this.generation = generation;
        this.bestResult = bestResult;
        this.averageResult = averageResult;
        this.worstResult = worstResult;
    }

    @CsvBindByName(column = "First Name", required = true)
    @CsvBindByPosition(position = 0)
    private int generation;
    @CsvBindByPosition(position = 1)
    private Integer worstResult;
    @CsvBindByPosition(position = 2)
    private Double averageResult;
    @CsvBindByPosition(position = 3)
    private Integer bestResult;

    public Integer getBestResult() {
        return bestResult;
    }

    public void setBestResult(Integer bestResult) {
        this.bestResult = bestResult;
    }

    public Double getAverageResult() {
        return averageResult;
    }

    public void setAverageResult(Double averageResult) {
        this.averageResult = averageResult;
    }

    public Integer getWorstResult() {
        return worstResult;
    }

    public void setWorstResult(Integer worstResult) {
        this.worstResult = worstResult;
    }

    public int getGeneration() {
        return generation;
    }

    public void setGeneration(int generation) {
        this.generation = generation;
    }
}
