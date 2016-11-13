package com.wzh.charts.view;

/**
 * Created by zhihao.wen on 2016/11/9.
 */

public class CircleRound {
    private double startAngel ;
    private double endAngel ;
    private double angel ;

    public double getStartAngel() {
        return startAngel;
    }

    public void setStartAngel(double startAngel) {
        this.startAngel = startAngel;
    }

    public double getEndAngel() {
        return endAngel;
    }

    public void setEndAngel(double endAngel) {
        this.endAngel = endAngel;
    }

    public double getAngel() {
        return endAngel-startAngel;
    }

    public double getAvgAngel() {
        return (endAngel+startAngel)/2;
    }

    @Override
    public String toString() {
        return "CircleRound{" +
                "startAngel=" + startAngel +
                ", endAngel=" + endAngel +
                ", angel=" + angel +
                '}';
    }
}
