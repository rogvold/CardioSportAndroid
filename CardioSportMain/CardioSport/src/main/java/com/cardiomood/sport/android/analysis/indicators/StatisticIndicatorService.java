package com.cardiomood.sport.android.analysis.indicators;

import java.util.List;

/**
 * Created by danshin on 07.10.13.
 */
public class StatisticIndicatorService extends AbstractIndicatorService {

    public StatisticIndicatorService(List<Integer> intervals) {
        super(intervals);
    }

    public StatisticIndicatorService() {

    }



    /**
     * Average
     *
     * @return
     */
    public double getRRNN() {
        double sum = 0;
        int k = 0;
        List<Integer> list = getIntervalsInDuration();
        for (Integer i : list) {
            if (sum > duration) {
                break;
            }
            sum += i;
            k++;
        }
        return Math.floor((1.0 * sum / k) * 100.0) / 100.0;
    }

    public double getSDNN() {
        double total = 0;
        List<Integer> list = getIntervalsInDuration();
        double average = getRRNN();
        for (Integer integer : list) {
            total += (average - integer) * (average - integer);
        }
        return Math.floor(Math.sqrt(total / list.size()) * 100.0) / 100.0;
    }


}
