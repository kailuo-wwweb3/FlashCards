package com.example.flashcards;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class Activity_aboutus extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_aboutus, menu);
        return true;
    }
}
