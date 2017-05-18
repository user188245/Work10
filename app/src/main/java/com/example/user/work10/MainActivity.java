package com.example.user.work10;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    MyDrawingView myDrawingView;
    CheckBox stamp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        myDrawingView = (MyDrawingView) findViewById(R.id.myDrawing);
        stamp = (CheckBox) findViewById(R.id.stamp);
        stamp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                myDrawingView.setParam((String)buttonView.getTag(),isChecked);
            }
        });
    }

    @Override
    public void onClick(View v){
        if((v.getTag().equals("ROTATE") || v.getTag().equals("MOVE") || v.getTag().equals("SCALE") || v.getTag().equals("SKEW"))) {
            stamp.setChecked(true);

        }
        myDrawingView.setParam((String)v.getTag(),true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0,0,0,"Blurring").setCheckable(true);
        menu.add(0,1,0,"Coloring").setCheckable(true);
        menu.add(0,2,0,"Pen Width Big").setCheckable(true);
        menu.add(0,3,0,"Pen Color RED");
        menu.add(0,4,0,"Pen Color BLUE");
        menu.add(0,5,0,"Pen Color RAINBOW");

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case 0:
                toggleChecking(item);
                myDrawingView.setParam("BLURRING",item.isChecked());
                break;
            case 1:
                toggleChecking(item);
                myDrawingView.setParam("COLORING",item.isChecked());
                break;
            case 2:
                toggleChecking(item);
                myDrawingView.setParam("BOLD",item.isChecked());
                break;
            case 3:
                myDrawingView.setParam("RED",false);
                break;
            case 4:
                myDrawingView.setParam("BLUE",false);
                break;
            case 5:
                myDrawingView.setParam("RAINBOW",false);
                break;

        }

        return false;
    }

    private void toggleChecking(MenuItem item){
        if(item.isChecked())
            item.setChecked(false);
        else
            item.setChecked(true);
    }




}