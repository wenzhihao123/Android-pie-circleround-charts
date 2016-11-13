package com.wzh.charts.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.wzh.charts.R;
import com.wzh.charts.view.CircleRound;
import com.wzh.charts.view.CircleRoundView;

/**
 * Created by zhihao.wen on 2016/11/11.
 */
public class CircleRoundActivity extends FragmentActivity{
    private CircleRoundView circleRoundView ;
    private String[] lable = {"2012","2013", "2014","2015","2016","2017"};
    private int[] colors = {Color.parseColor("#ff0033"),Color.parseColor("#99ffff"),
            Color.parseColor("#66ff33"),Color.parseColor("#FFEC8B"),
            Color.parseColor("#FF83FA"),Color.parseColor("#AEEEEE"),
            Color.parseColor("#912CEE"),Color.parseColor("#595959"),
            Color.parseColor("#AEEEEE"),Color.parseColor("#912CEE")};
    private double[] data = {10d, 10,10d, 10d};
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle);
        circleRoundView = (CircleRoundView)findViewById(R.id.circleRound);
        circleRoundView.setColors(colors);
        circleRoundView.setData(data);
        circleRoundView.setLable(lable);
        circleRoundView.setLegendColumn(3);

    }
}
