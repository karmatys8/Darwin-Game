package agh.ics.oop.model.util;

import java.text.DecimalFormat;

import static java.lang.StrictMath.round;

public class Average {
    private int sum;
    private int numberOfElements;

    public Average(){
        this.sum = 0;
        this.numberOfElements = 0;
    }
    public void add(int value){
        sum += value;
        numberOfElements ++;
    }

    public int getAverage() {
        if (numberOfElements == 0) { return 0; }

        int average = (int) round((double) sum / (double) numberOfElements);

        return average;
    }
}
