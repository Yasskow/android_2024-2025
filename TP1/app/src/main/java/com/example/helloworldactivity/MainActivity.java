package com.example.helloworldactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<City> cities;
    private int cpt = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cities = City.loadFromAsset(this, R.raw.topcities);
        System.out.println(cities);

        ImageView imgView = findViewById(R.id.imageView);
        TextView textView = findViewById(R.id.textView);
        TextView textView2 = findViewById(R.id.textView);
        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cpt >= 0 && cpt < 10){
                    textView.setBackgroundColor(Color.BLACK);
                } else if (cpt >= 10 && cpt < 20) {
                    textView.setBackgroundColor(Color.BLUE);
                } else if (cpt >= 20 && cpt < 29) {
                    textView.setBackgroundColor(Color.RED);
                }
                cpt++;
                textView.setText(String.valueOf(cpt));
                textView.setTextColor(Color.WHITE);
            }
        });

        imgView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                    Log.i(this.getClass().getName(), "action_down: " + event.getX() + ", " + event.getY());
                    float latitude = 90 - ((event.getY()/imgView.getHeight()) * 180);
                    float longitude = ((event.getX()/imgView.getWidth()) * 360) - 180;
                    City res = City.findNearest(cities, latitude, longitude);
                    textView2.setText(res.toString());
                    System.out.println(latitude + " : " + longitude);
                }
                return true; // important to return true to continue to receive the following events
            }
        });
    }

    public void onQuitClicked(View view){
        Toast t = Toast.makeText(this, R.string.let_s_quit_the_activity, Toast.LENGTH_SHORT);
        t.show();
        finish();
    }
}