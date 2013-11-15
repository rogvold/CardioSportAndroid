package com.cardiomood.sport.android.analysis.indicators;

import com.cardiomood.sport.android.analysis.indicators.utils.Histogram;

import java.util.Collections;
import java.util.List;

/**
 * Created by danshin on 07.10.13.
 */
public class HRVIndicatorService extends AbstractIndicatorService {

    public HRVIndicatorService(List<Integer> intervals) {
        super(intervals);
    }

    public HRVIndicatorService() {
    }

    public double getAMoPercents() {
        List<Integer> list = getIntervalsInDuration();
        Histogram h = new Histogram(list.size()).init();
        for (Integer interval : list) {
            h.addRRInterval(interval);
        }
        int maxRangeValue = h.getMaxIntervalNumber();
        int totalCount = h.getTotalCount();
        return Math.floor((maxRangeValue / (double) totalCount) * 10000) / 100.0;
    }

    public double getIN() {
        double bp = getBP();
        double amo = getAMoPercents();
        double mo = getMo();
        return Math.floor((amo * 1000 * 1000 / (2 * bp * mo)) * 100) / 100.0;
    }

    public double getBP() {
        List<Integer> list = getIntervalsInDuration();
        return (Collections.max(list) - Collections.min(list));
    }

    public double getMo() {
        List<Integer> list = getIntervalsInDuration();
        Histogram h = new Histogram(list.size()).init();
        for (Integer interval : list) {
            h.addRRInterval(interval);
        }
        return h.getMaxIntervalStart();
    }
}