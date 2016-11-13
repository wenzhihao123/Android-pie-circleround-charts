package com.wzh.charts.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.wzh.charts.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void click(View v){
        if (v.getId()==R.id.circleRound){
            startActivity(new Intent(MainActivity.this,CircleRoundActivity.class));
        }else if (v.getId()==R.id.pie){

        }
    }
}
