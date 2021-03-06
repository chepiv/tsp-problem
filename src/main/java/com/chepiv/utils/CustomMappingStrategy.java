package com.chepiv.utils;

import com.opencsv.bean.ColumnPositionMappingStrategy;

class CustomMappingStrategy<T> extends ColumnPositionMappingStrategy<T> {
    private static final String[] HEADER = new String[]{"generation", "worstResult", "averageResult", "bestResult", "temp"};

    @Override
    public String[] generateHeader() {
        return HEADER;
    }
}