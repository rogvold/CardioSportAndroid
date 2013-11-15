package com.cardiomood.sport.android.analysis.baevsky;

import java.util.List;

/**
 * Created by danshin on 07.10.13.
 */
public abstract class Characteristics {

    protected String name;
    protected List<Integer> rates;

    public Characteristics(List<Integer> rates){
        this.rates = rates;
    }

    public abstract String getName();

    public abstract CharacteristicsScore getResult();


    public List<Integer> getRates() {
        return rates;
    }

    public void setRates(List<Integer> rates) {
        this.rates = rates;
    }

}