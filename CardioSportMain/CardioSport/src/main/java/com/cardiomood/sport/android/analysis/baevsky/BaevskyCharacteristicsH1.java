package com.cardiomood.sport.android.analysis.baevsky;

import com.cardiomood.sport.android.analysis.indicators.StatisticIndicatorService;

import java.util.List;

/**
 * Created by danshin on 07.10.13.
 */
public class BaevskyCharacteristicsH1 extends Characteristics {

    public BaevskyCharacteristicsH1(List<Integer> rates) {
        super(rates);
    }

    @Override
    public String getName() {
        return "The total effect of regulation";
    }

    @Override
    public CharacteristicsScore getResult() {
        StatisticIndicatorService sis = new StatisticIndicatorService(rates);
        double m = sis.getRRNN();
        if (m <= 660) {
            return new CharacteristicsScore(2, "Expressed tachycardia","Выражденная тахикардия");
        }

        if (m <= 800) {
            return new CharacteristicsScore(1, "Moderate tachycardia", "Умеренная тахикардия");
        }

        if ((m > 800) && (m < 1000)) {
            return new CharacteristicsScore(0, "Normokardia", "Нормокардия");
        }

        if (m >= 1200) {
            return new CharacteristicsScore(-2, "Expressed bradykardia", "Вырожденная барикардия");
        }

        if (m >= 1000) {
            return new CharacteristicsScore(-1, "Moderate bradykardia","Умеренная брадикардия");
        }

        return null;
    }
}
