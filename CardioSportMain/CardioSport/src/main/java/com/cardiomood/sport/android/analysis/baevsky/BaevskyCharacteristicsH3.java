package com.cardiomood.sport.android.analysis.baevsky;

import com.cardiomood.sport.android.analysis.indicators.HRVIndicatorService;
import com.cardiomood.sport.android.analysis.indicators.StatisticIndicatorService;

import java.util.List;

/**
 * Created by danshin on 07.10.13.
 */
public class BaevskyCharacteristicsH3 extends Characteristics {

    public BaevskyCharacteristicsH3(List<Integer> rates) {
        super(rates);
    }

    @Override
    public String getName() {
        return "vegetative homeostasis";
    }

    @Override
    public CharacteristicsScore getResult() {
        HRVIndicatorService hrv = new HRVIndicatorService(rates);
        StatisticIndicatorService sis = new StatisticIndicatorService(rates);
        double Imo = hrv.getAMoPercents();
        double delta = hrv.getBP();
        double N = hrv.getIN();

        if ((delta <= 60) && (Imo >= 80) && (N >= 500)) {
            return new CharacteristicsScore(2, "Expressed predominance of the sympathetic nervous system");
        }

        if ((delta <= 150) && (Imo >= 50) && (N >= 200)) {
            return new CharacteristicsScore(1, "Moderate predominance of the sympathetic nervous system");
        }

        if ((delta >= 500) && (Imo <= 15) && (N <= 25)) {
            return new CharacteristicsScore(-2, "Expressed predominance of the parasympathetic nervous system");
        }

        if ((delta >= 300) && (Imo <= 30) && (N <= 50)) {
            return new CharacteristicsScore(-1, "Moderate predominance of the parasympathetic nervous system");
        }

        return new CharacteristicsScore(0, "Vegetative homeostasis saved");
    }
}
