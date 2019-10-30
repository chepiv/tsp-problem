package com.chepiv.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chepiv on 30/10/2019.
 * Contact: chepurin.ivan@gmail.com
 * Github:chepiv
 */
public class TabuList {
    private List<Genome> tabuList;
    private int size;

    public TabuList( int size) {
        this.tabuList = new ArrayList<>();
        this.size = size;
    }

    public void add(Genome genome) {
        tabuList.add(genome);
        if (tabuList.size() > size) {
            tabuList.remove(tabuList.size() - 1);
        }
    }

    public boolean contains(Genome genome) {
        return tabuList.contains(genome);
    }

    public List<Genome> getTabuList(){
        return tabuList;
    }
}
