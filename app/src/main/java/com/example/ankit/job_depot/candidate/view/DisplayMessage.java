package com.example.ankit.job_depot.candidate.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;


public class DisplayMessage extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_display_message);
        Intent intent=getIntent();
        String message=intent.getStringExtra(MainActivity.WELCOME_MESSAGE);
        TextView textview=new TextView(this);
        textview.setTextSize(30);
        textview.setText(message);
        setContentView(textview);

    }

}
