package com.cardiomood.sport.android.analysis.indicators;

import com.cardiomood.sport.android.analysis.indicators.utils.Training;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by danshin on 07.10.13.
 */
public abstract class AbstractIndicatorService {

    public static final int DEFAULT_DURATION = 5 * 60 * 1000;
    public static final int LOWER_BORDER = 400;
    public static final int UPPER_BORDER = 1300;
    protected List<Integer> intervals;
    protected int duration = DEFAULT_DURATION;
    protected Training training;

    public AbstractIndicatorService(List<Integer> intervals) {
        this.intervals = intervals;
        this.training = new Training("no id", intervals);
    }

    public AbstractIndicatorService(String id,List<Integer> intervals){
        this.intervals = intervals;
        this.training = new Training(id, intervals);
    }

    public AbstractIndicatorService() {
    }

    public void setIntervals(List<Integer> list) {
        this.intervals = list;
        this.training = new Training("no id", list);
    }

    public void setDuration(int newDuration) {
        this.duration = newDuration;
    }

    public int getDuration() {
        return duration;
    }

    public List<Integer> getIntervals() {
        return intervals;
    }

    protected List<Integer> getIntervalsInDuration() {
        int sum = 0;
        List<Integer> list = new ArrayList<Integer>();
        for (Integer i : intervals) {
            if ((i < LOWER_BORDER) || (i > UPPER_BORDER)) {
                continue;
            }
            if (sum > duration) {
                break;
            }
            sum += i;
            list.add(i);
        }
        return list;
    }

    public Double parameter(String parameterName) throws Exception {
        Method m = this.getClass().getMethod("get" + parameterName);
        Double result = Double.parseDouble(m.invoke(this).toString());
        return result;
    }

    public List<String> allParametersNames() {
        List<String> list = new ArrayList();
        Method[] methods = this.getClass().getMethods();
        for (Method m : methods) {
            int modif = m.getModifiers();
            if ((Modifier.isPublic(modif)) && (m.getName().indexOf("get") == 0) && (!m.getName().equals("getClass")) && (!m.getName().equals("getDuration")) && (!m.getName().equals("getIntervals"))) { // if public getter then invoke
                list.add(m.getName().substring(3));
            }
        }
        return list;
    }

    /**
     *
     * @return Map<имя параметра, значение параметра>
     * @throws Exception
     */
    public Map<String, Double> allCalculatedParameters() throws Exception {
        Method[] methods = this.getClass().getMethods();
        Map<String, Double> map = new HashMap<String, Double>();
        for (Method m : methods) {
            int modif = m.getModifiers();
            if ((Modifier.isPublic(modif)) && (m.getName().indexOf("get") == 0) && (!m.getName().equals("getClass")) && (!m.getName().equals("getDuration")) && (!m.getName().equals("getIntervals"))) { // if public getter then invoke
                System.out.println("try to invoke method " + m.getName());
                Object result = m.invoke(this).toString();
                map.put(m.getName().substring(3), Double.parseDouble(result.toString()));
            }
        }
        return map;
    }
}
