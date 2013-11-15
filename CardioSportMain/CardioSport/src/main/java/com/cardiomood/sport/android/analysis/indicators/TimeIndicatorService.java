package com.cardiomood.sport.android.analysis.indicators;

import java.util.List;

/**
 * Created by danshin on 07.10.13.
 */
public class TimeIndicatorService extends AbstractIndicatorService{

    public TimeIndicatorService(List<Integer> intervals) {
        super(intervals);
    }

    public TimeIndicatorService() {
    }



    public double getPNN50(){
        int pnn = 0;
        for (int i = 1; i < intervals.size(); i++) {
            Integer now = intervals.get(i);
            Integer before = intervals.get(i-1);
            if (Math.abs(now - before) >= 50) {
                pnn++;
            }
        }

        return Math.floor(((double) pnn) / (intervals.size() - 1) * 10000)/100.0;
    }

}
