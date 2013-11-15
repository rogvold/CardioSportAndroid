package com.cardiomood.sport.android.analysis.indicators;

import com.cardiomood.sport.android.analysis.indicators.utils.Lomb;
import com.cardiomood.sport.android.analysis.indicators.utils.Periodogram;
import com.cardiomood.sport.android.analysis.indicators.utils.Square;

import java.util.List;

/**
 * Created by danshin on 07.10.13.
 */
public class SpectrumIndicatorService extends AbstractIndicatorService {

    public SpectrumIndicatorService() {
    }

    public SpectrumIndicatorService(List<Integer> intervals) {
        super(intervals);
    }

    public SpectrumIndicatorService(String id, List<Integer> intervals) {
        super(id, intervals);
    }

    public double getHFPercents() {
        List<Periodogram> periodogram = training.evaluate(new Lomb());
        return new Square(periodogram, 0.15, 0.4).Calculate();
    }

    public double getLFPercents() {
        List<Periodogram> periodogram = this.training.evaluate(new Lomb());
        return new Square(periodogram, 0.04, 0.15).Calculate();
    }

    public double getVLFPercents() {
        List<Periodogram> periodogram = training.evaluate(new Lomb());
        return new Square(periodogram, 0.0033, 0.04).Calculate();
    }

    public double getULFPercents() {
        List<Periodogram> periodogram = training.evaluate(new Lomb());
        return new Square(periodogram, 0, 0.0033).Calculate();
    }

    public double getSdPercents() {
        List<Periodogram> periodogram = training.evaluate(new Lomb());
        return new Square(periodogram, 0.1, 0.5).Calculate();
    }

    public double getS1Percents() {
        List<Periodogram> periodogram = training.evaluate(new Lomb());
        return new Square(periodogram, 0.0333, 0.1).Calculate();
    }

    public double getS2Percents() {
        List<Periodogram> periodogram = training.evaluate(new Lomb());
        return new Square(periodogram, 0.0167, 0.0333).Calculate();
    }

    public double getTP() {
        //TODO: implement =)
        return 0;
    }

    public double getIC() {
        return Math.floor((getHFPercents() + getLFPercents()) * 100.0 / getVLFPercents()) / 100.0;
    }
}
