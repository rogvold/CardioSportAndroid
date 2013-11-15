package com.cardiomood.sport.android.analysis.baevsky;

import com.cardiomood.sport.android.analysis.indicators.HRVIndicatorService;
import com.cardiomood.sport.android.analysis.indicators.StatisticIndicatorService;

import java.util.List;

/**
 * Created by danshin on 07.10.13.
 */
public class BaevskyCharacteristicsH2 extends Characteristics {

    public BaevskyCharacteristicsH2(List<Integer> rates) {
        super(rates);
    }

    @Override
    public String getName() {
        return "function of automatism";
    }

    @Override
    public CharacteristicsScore getResult() {
        HRVIndicatorService hrv = new HRVIndicatorService(rates);
        StatisticIndicatorService sis = new StatisticIndicatorService(rates);
        double m = sis.getRRNN();
        double sigma = sis.getSDNN();
        double delta = hrv.getBP();
        double v = 100.0 * sigma / m;

        if ((sigma < 100) && (delta >= 600) && (v < 8)) {
            return new CharacteristicsScore(-2, "Expressed violation of automatism");
        }

        if ((delta >= 450)) {
            return new CharacteristicsScore(-1, "Moderate violation of automatism");
        }

        if ((sigma <= 20) && (delta <= 100) && (v <= 2)) {
            return new CharacteristicsScore(2, "Stable rhythm");
        }

        if ((sigma >= 100) && (delta >= 300) && (v >= 8)) {
            return new CharacteristicsScore(1, "Expressed sinus arrhythmia");
        }

        return new CharacteristicsScore(0, "Moderate sinus arrhythmia");
    }
}
