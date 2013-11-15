package com.cardiomood.sport.android.analysis.baevsky;

import com.cardiomood.sport.android.analysis.indicators.HRVIndicatorService;
import com.cardiomood.sport.android.analysis.indicators.StatisticIndicatorService;

import java.util.List;

/**
 * Created by danshin on 07.10.13.
 */
public class BaevskyCharacteristicsH4 extends Characteristics {

    public BaevskyCharacteristicsH4(List<Integer> rates) {
        super(rates);
    }

    @Override
    public String getName() {
        return "stability of regulation";
    }

    @Override
    public CharacteristicsScore getResult() {
        HRVIndicatorService hrv = new HRVIndicatorService(rates);
        StatisticIndicatorService sis = new StatisticIndicatorService(rates);
        double m = sis.getRRNN();
        double sigma = sis.getSDNN();
        double v = 100.0 * sigma / m;

        if (v <= 3) {
            return new CharacteristicsScore(2, " dysregulation");
        }

        if (v >= 6) {
            return new CharacteristicsScore(-2, " dysregulation");
        }

        return new CharacteristicsScore(0, "Stable dysregulation");
    }
}
